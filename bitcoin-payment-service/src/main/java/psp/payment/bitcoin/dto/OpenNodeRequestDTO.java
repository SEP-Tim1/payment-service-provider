package psp.payment.bitcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpenNodeRequestDTO {

    private double amount;
    private String currency;
    private String order_id;
    private String callback_url;
    private String success_url;
}
