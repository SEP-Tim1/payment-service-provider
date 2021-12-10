package psp.payment.card.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long storeId;

    @Column
    private boolean cardPaymentEnabled;

    public Card(long storeId, boolean cardPaymentEnabled) {
        this.storeId = storeId;
        this.cardPaymentEnabled = cardPaymentEnabled;
    }
}
