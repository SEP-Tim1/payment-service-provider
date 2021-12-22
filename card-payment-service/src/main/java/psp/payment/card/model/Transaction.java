package psp.payment.card.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String transactionStatus;
    @Column
    private long merchantOrderId;
    @Column
    private long requestId;
    @Column
    private long acquirerOrderId;
    @Column
    private LocalDateTime acquirerTimestamp;
    @Column
    private long paymentId;
    @Column
    private String errorMessage;

    public Transaction(String transactionStatus, long merchantOrderId, long requestId, long acquirerOrderId, LocalDateTime acquirerTimestamp, long paymentId, String errorMessage) {
        this.transactionStatus = transactionStatus;
        this.merchantOrderId = merchantOrderId;
        this.requestId = requestId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
        this.errorMessage = errorMessage;
    }
}
