package psp.request.controllers;

import org.springframework.web.bind.annotation.*;
import psp.request.dtos.PaymentRequestDTO;
import psp.request.dtos.PaymentResponseIdDTO;
import psp.request.exceptions.NotFoundException;
import psp.request.model.PaymentOutcome;
import psp.request.model.PaymentRequest;
import psp.request.services.PaymentRequestService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("request")
public class PaymentRequestController {

    private final PaymentRequestService service;

    public PaymentRequestController(PaymentRequestService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponseIdDTO create(HttpServletRequest request, @RequestBody PaymentRequestDTO dto) throws NotFoundException {
        return service.create(request, dto);
    }

    @GetMapping("failure/{requestId}")
    public String getFailureUrl(@PathVariable long requestId) throws NotFoundException {
        return service.getFailureUrl(requestId);
    }

    @GetMapping("{id}")
    public PaymentRequest getById(@PathVariable long id) throws NotFoundException {
        return service.getById(id);
    }

    @PostMapping("outcome/{id}")
    public void setOutcome(HttpServletRequest request, @PathVariable long id, @RequestBody PaymentOutcome outcome) throws NotFoundException {
        service.setOutcome(request, id, outcome);
    }

    @GetMapping("processed/{id}")
    public boolean isProcessed(@PathVariable long id) throws NotFoundException {
        return service.isProcessed(id);
    }
}
