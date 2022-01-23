package psp.payment.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MerchantSubscriptionDTO {

    @Pattern(regexp = "^[A-Za-z0-9 -_]+$", message = "Client ID can contain only letters, digits, blank spaces and a - or _ character")
    private String clientId;
    @Pattern(regexp = "^[A-Za-z0-9 -_]+$", message = "Client Secret can contain only letters, digits, blank spaces and a - or _ character")
    private String clientSecret;
}
