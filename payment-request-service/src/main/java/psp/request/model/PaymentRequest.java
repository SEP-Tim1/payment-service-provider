package psp.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long storeId;
    @Column
    private long merchantOrderId;
    @Column
    private LocalDateTime merchantTimestamp;
    @Column
    private float amount;
    @Column
    private String successUrl;
    @Column
    private String failureUrl;
    @Column
    private String errorUrl;
    @Column
    private String callbackUrl;
    @Embedded
    private PaymentOutcome outcome;

    public PaymentRequest(long storeId, long merchantOrderId, LocalDateTime merchantTimestamp, float amount, String successUrl, String failureUrl, String errorUrl, String callbackUrl) {
        this.storeId = storeId;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.amount = amount;
        this.successUrl = successUrl;
        this.failureUrl = failureUrl;
        this.errorUrl = errorUrl;
        this.callbackUrl = callbackUrl;
    }
}
