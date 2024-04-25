package by.tms.onlinerclone26onl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private double price;
    private String address;
    private String seller;
    private ProductType type;

    public Product() {

    }
}
