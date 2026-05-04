package model;

import java.util.List;

public class Items {

private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    private String color;

private List<String> equipment;

}


//{
// "title": "order",
// "ID": 123456.
//"category": "cars",
//"items":{
//  "model": "Tesla X"
//  "color": "blue"
//  "equipment": ["engine", "braking system", "wheels"]
//  }
// }