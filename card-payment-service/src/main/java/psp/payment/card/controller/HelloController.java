package psp.payment.card.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/{name}")
    public ResponseEntity<String> hello(@PathVariable String name) {
        return new ResponseEntity<>(port + " Hello " + name, HttpStatus.OK);
    }
}
