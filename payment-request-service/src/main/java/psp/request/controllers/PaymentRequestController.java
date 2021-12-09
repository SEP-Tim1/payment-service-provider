package psp.request.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psp.request.dtos.PaymentRequestDTO;
import psp.request.dtos.PaymentResponseDTO;
import psp.request.exceptions.NotFoundException;
import psp.request.services.PaymentRequestService;

@RestController
@RequestMapping("request")
public class PaymentRequestController {

    @Autowired
    private PaymentRequestService service;

    @PostMapping
    public PaymentResponseDTO create(@RequestBody PaymentRequestDTO dto) throws NotFoundException {
        return service.create(dto);
    }
}
