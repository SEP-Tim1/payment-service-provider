package psp.payment.bitcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChargeStatusDTO {

    private String id;
    private String callback_url;
    private String success_url;
    private String status;
    private String order_id;
    private String description;
    private double price;
    private double fee;
    private boolean auto_settle;
    private String hashed_order;
}
