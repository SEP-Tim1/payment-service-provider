package psp.payment.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.payment.paypal.util.SensitiveDataConverter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MerchantSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long storeId;
    @Column
    private String clientId;
    @Column
    @Convert(converter = SensitiveDataConverter.class)
    private String clientSecret;

    public MerchantSubscription(long storeId, String clientId, String clientSecret) {
        this.storeId = storeId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
