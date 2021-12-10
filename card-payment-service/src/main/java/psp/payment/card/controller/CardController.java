package psp.payment.card.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psp.payment.card.dtos.PaymentRequest;
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
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Request not found.");
        }
    }
}
