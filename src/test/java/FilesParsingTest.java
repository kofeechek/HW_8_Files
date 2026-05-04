import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;


public class FilesParsingTest {

    private ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("testzip.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String[] fileData = entry.getName().split("\\.");
                String fileType = fileData[fileData.length - 1];


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                zis.transferTo(baos);
                byte[] fileBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);

                switch (fileType) {
                    case "csv":
                        csvFileParsing(bais);
                        break;
                    case "xls":
                        xlsxFileParsing(bais);
                        break;
                    case "pdf":
                        pdfFileParsing(bais);
                        break;
                }
            }

        }
    }


    private void csvFileParsing(InputStream inputStream) throws Exception {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {

            List<String[]> data = csvReader.readAll();
            assertEquals(3, data.size());
            Assertions.assertArrayEquals(
                    new String[]{"January", "first month of the year"},
                    data.get(0)
            );
            Assertions.assertArrayEquals(
                    new String[]{"February", "second month of the year"},
                    data.get(1)
            );
            Assertions.assertArrayEquals(
                    new String[]{"March", "third month of the year"},
                    data.get(2)
            );
        }
    }

    private void pdfFileParsing(InputStream inputStream) throws Exception {
        try (PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            assertTrue(text.contains("Извещение о дорожно-транспортном происшествии"));


        }
    }

    private void xlsxFileParsing(InputStream inputStream) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheet("Payments");
            Row row = sheet.getRow(1);
            Cell cell = row.getCell(3);

            String value = cell.getStringCellValue();

            assertEquals("42301810158742897803", value);
        }
    }

    @Test
    void jsonFileParsingTest() throws Exception {
        Path path = Path.of(getClass().getClassLoader().getResource("order.json").toURI());
        String json = Files.readString(path);

        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(json, Order.class);

        assertEquals("order", order.getTitle());
        assertEquals(123456, order.getId());
        assertEquals("cars", order.getCategory());

        assertNotNull(order.getItems());
        assertEquals("Tesla X", order.getItems().getModel());
        assertEquals("blue", order.getItems().getColor());
        assertEquals(3, order.getItems().getEquipment().size());
        assertTrue(order.getItems().getEquipment().contains("engine"));
    }
}







