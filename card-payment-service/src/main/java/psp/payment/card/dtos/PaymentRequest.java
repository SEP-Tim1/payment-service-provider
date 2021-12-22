package psp.payment.card.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentRequest {

    private long id;
    private long storeId;
    private long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private float amount;
    private String successUrl;
    private String failureUrl;
    private String errorUrl;
}
