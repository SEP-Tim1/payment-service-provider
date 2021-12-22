package psp.payment.card.dtos;

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
