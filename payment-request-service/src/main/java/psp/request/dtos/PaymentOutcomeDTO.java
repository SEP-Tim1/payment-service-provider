package psp.request.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.request.model.PaymentStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOutcomeDTO {

    private long merchantOrderId;
    private PaymentStatus status;
    private String message;
}
