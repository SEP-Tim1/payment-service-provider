package psp.payment.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.payment.paypal.util.SensitiveDataConverter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "^[A-Za-z0-9 -_]+$", message = "Client ID can contain only letters, digits, blank spaces and a - or _ character")
    private String clientId;
    @Column
    @Convert(converter = SensitiveDataConverter.class)
    @Pattern(regexp = "^[A-Za-z0-9 -_]+$", message = "Client Secret can contain only letters, digits, blank spaces and a - or _ character")
    private String clientSecret;

    public MerchantSubscription(long storeId, String clientId, String clientSecret) {
        this.storeId = storeId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
