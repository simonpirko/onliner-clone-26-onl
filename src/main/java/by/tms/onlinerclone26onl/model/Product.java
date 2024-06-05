package by.tms.onlinerclone26onl.model;


import by.tms.onlinerclone26onl.util.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Product {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private long quantity;
    private byte[] photo;
    private Map<String, String> characteristics;
    private Subcategory subcategory;

    public Product(String name, String description, BigDecimal price, byte[] bytes) {
    }

    public String getImageBase64() {
        return ImageUtils.bytesToBase64(photo);
    }
}
