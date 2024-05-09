package by.tms.onlinerclone26onl.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Product {
    private long id;
    private String name;
    private String description;
    private double price;
    private long quantity;
    private byte[] photo;
    private Map<String, String> characteristics;

    public Product(String name, String description, int price, byte[] bytes) {
    }
}
