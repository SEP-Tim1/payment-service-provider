package psp.payment.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MerchantSubscriptionDTO {

    private String clientId;
    private String clientSecret;
}
