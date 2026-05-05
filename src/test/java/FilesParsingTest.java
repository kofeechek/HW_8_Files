import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void csvFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("testzip.zip")
        )) {
            ZipEntry entry = zis.getNextEntry();

            assertNotNull(entry);

            do {

                String[] fileData = entry.getName().split("\\.");
                String fileType = fileData[fileData.length - 1];


                if (fileType != "csv") {
                    continue;
                }
                try (CSVReader csvReader = new CSVReader(new InputStreamReader(zis))) {

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
            } while ((entry = zis.getNextEntry()) != null);


        }
    }


    @Test
    void pdfFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("testzip.zip")
        )) {
            ZipEntry entry = zis.getNextEntry();

            assertNotNull(entry);

            do {

                String[] fileData = entry.getName().split("\\.");
                String fileType = fileData[fileData.length - 1];


                if (fileType != "pdf") {
                    continue;
                }
                try (PDDocument document = PDDocument.load(zis)) {

                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    String text = pdfStripper.getText(document);

                    assertTrue(text.contains("Извещение о дорожно-транспортном происшествии"));
                }
            } while ((entry = zis.getNextEntry()) != null);
        }
    }


    @Test
    void xlsxFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("testzip.zip")
        )) {
            ZipEntry entry = zis.getNextEntry();

            assertNotNull(entry);

            do {

                String[] fileData = entry.getName().split("\\.");
                String fileType = fileData[fileData.length - 1];


                if (fileType != "xlsx") {
                    continue;
                }
                try (Workbook workbook = WorkbookFactory.create(zis)) {

                    Sheet sheet = workbook.getSheet("Payments");
                    Row row = sheet.getRow(1);
                    Cell cell = row.getCell(3);

                    String value = cell.getStringCellValue();

                    assertEquals("42301810158742897803", value);
                }
            } while ((entry = zis.getNextEntry()) != null);
        }
    }


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







