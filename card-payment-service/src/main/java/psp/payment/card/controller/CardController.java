package psp.payment.card.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psp.payment.card.client.AuthClient;
import psp.payment.card.client.StoreClient;
import psp.payment.card.dtos.IsEnabledDTO;
import psp.payment.card.dtos.MerchantInfoDTO;
import psp.payment.card.dtos.PaymentRequest;
import psp.payment.card.dtos.PaymentResponseDTO;
import psp.payment.card.exceptions.*;
import psp.payment.card.service.CardService;
import psp.payment.card.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("card")
@Slf4j
public class CardController {

    private final CardService cardService;
    private final AuthClient authClient;
    private final StoreClient storeClient;
    private final LoggerUtil loggerUtil;

    @Autowired
    public CardController(CardService cardService, AuthClient authClient, StoreClient storeClient, LoggerUtil loggerUtil) {
        this.cardService = cardService;
        this.authClient = authClient;
        this.storeClient = storeClient;
        this.loggerUtil = loggerUtil;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> isCardEnabled(@PathVariable long requestId){
        try {
            PaymentRequest request = cardService.getByRequestId(requestId);
            if (!cardService.paymentEnabledForRequest(request)) {
                return ResponseEntity.badRequest().body("Request has already been processed");
            }
            if (!request.getBillingCycle().equals("ONE_TIME")) {
                return ResponseEntity.badRequest().body("Subscriptions not supported");
            }
            return ResponseEntity.ok(cardService.getByStoreId(request));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Request not found.");
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<?> isCardEnabledForStore(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to check card payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to check card payment subscription"));
            throw new UnauthorizedException("You don't have a permission to check bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            return ResponseEntity.ok(cardService.getByStoreId(storeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @PostMapping("/enable/{storeId}")
    public ResponseEntity<?> enable(HttpServletRequest request, @RequestBody @Valid MerchantInfoDTO dto, @RequestHeader("Authorization") String token) throws UnauthorizedException, UnauthenticatedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to create card payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to create card payment subscription"));
            throw new UnauthorizedException("You don't have a permission to create card payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            cardService.enable(request, dto, storeId);
            return ResponseEntity.ok(new IsEnabledDTO(true));
        } catch (MerchantCredentialsNotValidException e) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }
    }

    @GetMapping("/disable/{storeId}")
    public ResponseEntity<?> disable(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to delete card payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to delete card payment subscription"));
            throw new UnauthorizedException("You don't have a permission to delete bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            cardService.disable(request, storeId);
            return ResponseEntity.ok(new IsEnabledDTO(false));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @GetMapping("/invoice/{requestId}")
    public ResponseEntity<?> getInvoiceResponse(HttpServletRequest request, @PathVariable long requestId){
        try {
            return ResponseEntity.ok(cardService.getInvoiceResponse(request, requestId));
        } catch (RequestNotFoundException | StoreNotFoundException | InvoiceNotValidException e) {
            log.info(loggerUtil.getLogMessage(request, "Invoice for request (id=" + request + ") not created. Error: " + e.getMessage()));
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("bank-payment-response")
    public void setPaymentOutcome(@RequestBody PaymentResponseDTO dto){
        cardService.setPaymentOutcome(dto);
    }
}
