package by.tms.onlinerclone26onl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Subcategory {

    private long id;
    private String name;
    private Category category;

    public Subcategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }

}
