package psp.payment.card.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import psp.payment.card.util.SensitiveDataConverter;
import psp.payment.card.util.SensitiveDataLongConverter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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
    @Convert(converter = SensitiveDataLongConverter.class)
    private long mid;
    @Column
    @Nullable
    @Pattern(regexp = "^[A-Za-z0-9]{20}", message = "Merchant password can contain only letters and digits and must be 20 chars long")
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
