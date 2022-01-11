package psp.payment.bitcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataDTO {

    private String id;
    private String name;
    private String description;
    private long created_at;
    private String status;
    private String callback_url;
    private String success_url;
    private String order_id;
    private String notes;
    private String currency;
    private double source_fiat_value;
    private double fiat_value;
    private boolean auto_settle;
    private String notif_email;
    private String address;
    private double amount;
    private String uri;
}
