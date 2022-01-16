package psp.payment.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayPalAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long requestId;
    @Column
    private String billingPlanId;
    @Column
    private String agreementId;
    @Column
    private String status;

    public PayPalAgreement(long requestId, String billingPlanId, String agreementId, String status) {
        this.requestId = requestId;
        this.billingPlanId = billingPlanId;
        this.agreementId = agreementId;
        this.status = status;
    }
}
