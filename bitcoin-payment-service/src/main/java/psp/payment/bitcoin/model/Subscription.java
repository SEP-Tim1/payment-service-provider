package psp.payment.bitcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String apiKey;

    public Subscription(long storeId, String apiKey) {
        this.storeId = storeId;
        this.apiKey = apiKey;
    }
}