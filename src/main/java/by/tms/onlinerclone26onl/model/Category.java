package by.tms.onlinerclone26onl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Category {

    private long id;
    private String name;
    private List<Subcategory> subcategories;

}
