package psp.payment.card.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MerchantInfoDTO {
    private long mid;
    @Pattern(regexp = "^[A-Za-z0-9]{20}", message = "Merchant password can contain only letters and digits and must be 20 chars long")
    private String mpassword;
    private int bank;
}
