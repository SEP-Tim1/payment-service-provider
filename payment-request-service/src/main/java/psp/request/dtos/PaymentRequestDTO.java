package psp.request.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.request.model.BillingCycle;
import psp.request.model.Currency;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequestDTO {

    private String apiToken;
    private long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private float amount;
    private Currency currency;
    private BillingCycle billingCycle;
    private String successUrl;
    private String failureUrl;
    private String errorUrl;
    private String callbackUrl;
}
