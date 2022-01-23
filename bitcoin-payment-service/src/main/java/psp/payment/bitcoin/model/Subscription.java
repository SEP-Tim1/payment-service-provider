package psp.payment.bitcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.payment.bitcoin.util.SensitiveDataConverter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long storeId;
    @Column
    @Convert(converter = SensitiveDataConverter.class)
    @Pattern(regexp = "^[A-Za-z0-9 -_]+$", message = "API Key can contain only letters, digits, blank spaces and a - or _ character")
    private String apiKey;

    public Subscription(long storeId, String apiKey) {
        this.storeId = storeId;
        this.apiKey = apiKey;
    }
}
