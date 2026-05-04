package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Order {
    private String title;

    @JsonProperty("ID")
    private Integer id;

    private String category;

    private Items items;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }
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