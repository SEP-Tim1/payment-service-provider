package psp.payment.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import psp.payment.qrcode.util.SensitiveDataConverter;
import psp.payment.qrcode.util.SensitiveDataLongConverter;

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
    private boolean qrPaymentEnabled;
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

    private String name = "Pera Detlic";
    private String accountNumber = "170001040565300054";

    public Card(long storeId, boolean qrPaymentEnabled, long mid, String mpassword, int bank) {
        this.storeId = storeId;
        this.qrPaymentEnabled = qrPaymentEnabled;
        this.mid = mid;
        this.mpassword = mpassword;
        this.bank = bank;
    }

    public void update(boolean qrPaymentEnabled, long mid, String mpassword, int bank){
        this.setMpassword(mpassword);
        this.setMid(mid);
        this.setQrPaymentEnabled(qrPaymentEnabled);
        this.setBank(bank);
    }
}
