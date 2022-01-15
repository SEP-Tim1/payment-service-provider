package psp.payment.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayPalTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long requestId;
    @Column
    private String paypalPaymentId;
    @Column
    private String status;

    public PayPalTransaction(long requestId, String paypalPaymentId, String status) {
        this.requestId = requestId;
        this.paypalPaymentId = paypalPaymentId;
        this.status = status;
    }
}
