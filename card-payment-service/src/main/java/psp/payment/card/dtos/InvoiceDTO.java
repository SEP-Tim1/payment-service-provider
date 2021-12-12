package psp.payment.card.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.payment.card.model.Card;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDTO {
    private long mId;
    private String mPassword;
    private float amount;
    private long merchantOrderId;
    private long requestId;
    private LocalDateTime merchantTimestamp;
    private String successUrl;
    private String failureUrl;
    private String errorUrl;

    public InvoiceDTO(PaymentRequest request, Card card){
        this.mId = card.getMid();
        this.mPassword = card.getMpassword();
        this.amount = request.getAmount();
        this.merchantOrderId = request.getMerchantOrderId();
        this.requestId = request.getId();
        this.merchantTimestamp = request.getMerchantTimestamp();
        this.successUrl = request.getSuccessUrl();
        this.failureUrl = request.getFailureUrl();
        this.errorUrl = request.getErrorUrl();
    }
}

