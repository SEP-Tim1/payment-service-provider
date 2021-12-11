package psp.payment.card.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psp.payment.card.dtos.IsEnabledDTO;
import psp.payment.card.dtos.MerchantInfoDTO;
import psp.payment.card.dtos.PaymentRequest;
import psp.payment.card.exceptions.InvoiceNotValidException;
import psp.payment.card.exceptions.MerchantCredentialsNotValidException;
import psp.payment.card.exceptions.RequestNotFoundException;
import psp.payment.card.exceptions.StoreNotFoundException;
import psp.payment.card.service.CardService;

@RestController
@RequestMapping("card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> isCardEnabled(@PathVariable long requestId){
        try {
            PaymentRequest request = cardService.getByRequestId(requestId);
            return ResponseEntity.ok(cardService.getByStoreId(request));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Request not found.");
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<?> isCardEnabledForStore(@PathVariable long id){
        try {
            return ResponseEntity.ok(cardService.getByStoreId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @PostMapping("/enable/{storeId}")
    public ResponseEntity<?> enable(@RequestBody MerchantInfoDTO dto, @PathVariable long storeId){
        try {
            cardService.enable(dto, storeId);
            return ResponseEntity.ok(new IsEnabledDTO(true));
        } catch (MerchantCredentialsNotValidException e) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }
    }

    @GetMapping("/disable/{storeId}")
    public ResponseEntity<?> disable(@PathVariable long storeId){
        try {
            cardService.disable(storeId);
            return ResponseEntity.ok(new IsEnabledDTO(false));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @GetMapping("/invoice/{requestId}")
    public ResponseEntity<?> getInvoiceResponse(@PathVariable long requestId){
        try {
            return ResponseEntity.ok(cardService.getInvoiceResponse(requestId));
        } catch (RequestNotFoundException | StoreNotFoundException | InvoiceNotValidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
