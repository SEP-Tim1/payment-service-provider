package psp.payment.card.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import psp.payment.card.util.SensitiveDataConverter;

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

    @Column
    @Nullable
    @Convert(converter = SensitiveDataConverter.class)
    private long mid;

    @Column
    @Nullable
    @Convert(converter = SensitiveDataConverter.class)
    private String mpassword;

    @Column
    @Nullable
    private int bank;

    public Card(long storeId, boolean cardPaymentEnabled, long mid, String mpassword, int bank) {
        this.storeId = storeId;
        this.cardPaymentEnabled = cardPaymentEnabled;
        this.mid = mid;
        this.mpassword = mpassword;
        this.bank = bank;
    }

    public void update(boolean cardPaymentEnabled, long mid, String mpassword, int bank){
        this.setMpassword(mpassword);
        this.setMid(mid);
        this.setCardPaymentEnabled(cardPaymentEnabled);
        this.setBank(bank);
    }
}
