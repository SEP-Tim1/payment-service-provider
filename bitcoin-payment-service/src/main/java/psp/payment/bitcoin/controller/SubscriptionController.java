package psp.payment.bitcoin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    @GetMapping
    public String test() {
        return "test successful";
    }
}
