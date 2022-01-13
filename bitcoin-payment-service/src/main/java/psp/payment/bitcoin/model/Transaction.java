package psp.payment.bitcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long requestId;
    @Column
    private long merchantOrderId;
    @Column
    private String status;

    public Transaction(long requestId, long merchantOrderId, String status) {
        this.requestId = requestId;
        this.merchantOrderId = merchantOrderId;
        this.status = status;
    }
}
