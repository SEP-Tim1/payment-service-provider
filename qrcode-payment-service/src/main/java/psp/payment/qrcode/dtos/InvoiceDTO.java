package psp.payment.qrcode.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import psp.payment.qrcode.model.Card;
import psp.payment.qrcode.model.Currency;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDTO {
    private long mId;
    private String mPassword;
    private float amount;
    private Currency currency;
    private long merchantOrderId;
    private long requestId;
    private LocalDateTime merchantTimestamp;
    private String successUrl;
    private String failureUrl;
    private String errorUrl;
    private String callbackUrl;

    public InvoiceDTO(PaymentRequest request, Card card, String callbackUrl){
        this.mId = card.getMid();
        this.mPassword = card.getMpassword();
        this.amount = request.getAmount();
        this.currency = request.getCurrency();
        this.merchantOrderId = request.getMerchantOrderId();
        this.requestId = request.getId();
        this.merchantTimestamp = request.getMerchantTimestamp();
        this.successUrl = request.getSuccessUrl();
        this.failureUrl = request.getFailureUrl();
        this.errorUrl = request.getErrorUrl();
        this.callbackUrl = callbackUrl;
    }
}

