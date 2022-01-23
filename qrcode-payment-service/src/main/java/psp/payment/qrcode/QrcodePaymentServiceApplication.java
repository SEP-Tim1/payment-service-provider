package psp.payment.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QrcodePaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrcodePaymentServiceApplication.class, args);
	}

}
