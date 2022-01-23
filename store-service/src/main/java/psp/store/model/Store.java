package psp.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.store.util.SensitiveDataConverter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @Pattern(regexp = "^[A-Za-z0-9 ]{1,50}$", message = "Store name must contain between 1 and 50 characters and can contain only letters, digits and blank spaces")
    private String name;
    @Column
    private long userId;
    @Column(length = 512)
    @Convert(converter = SensitiveDataConverter.class)
    private String apiToken;

    public Store(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }
}
