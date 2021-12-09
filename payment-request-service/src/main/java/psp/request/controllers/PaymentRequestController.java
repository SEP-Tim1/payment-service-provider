package psp.request.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("failure/{requestId}")
    public String getFailureUrl(@PathVariable long requestId) throws NotFoundException {
        return service.getFailureUrl(requestId);
    }
}
