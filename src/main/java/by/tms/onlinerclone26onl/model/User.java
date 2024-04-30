package by.tms.onlinerclone26onl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String name;
    private String surname;
    private String password;
    private Boolean type;
    private List<Product> productList;
    private byte[] image;

}
