package psp.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOutcome {

    @Column
    private PaymentStatus status;
    @Column
    private String message;
}
