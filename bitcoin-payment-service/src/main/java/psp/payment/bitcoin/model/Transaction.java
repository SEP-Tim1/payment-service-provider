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
    private String openNodeId;

    public Transaction(long requestId, String openNodeId) {
        this.requestId = requestId;
        this.openNodeId = openNodeId;
    }
}
