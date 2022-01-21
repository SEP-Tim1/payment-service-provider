package psp.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.store.util.SensitiveDataConverter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private long userId;
    @Column
    @Convert(converter = SensitiveDataConverter.class)
    private String apiToken;

    public Store(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }
}
