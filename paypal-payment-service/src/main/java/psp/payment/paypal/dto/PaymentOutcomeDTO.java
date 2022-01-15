package psp.payment.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOutcomeDTO {

    private PaymentStatusDTO status;
    private String message;
}
