package psp.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psp.store.client.CardPaymentServiceClient;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    private CardPaymentServiceClient cardPaymentServiceClient;

    @GetMapping("/{name}")
    public ResponseEntity<String> hello(@PathVariable String name) {
        String response = cardPaymentServiceClient.hello(name);
        return new ResponseEntity<>("Store: " + response, HttpStatus.OK);
    }
}
