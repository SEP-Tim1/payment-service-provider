package psp.payment.qrcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psp.payment.qrcode.clients.AuthClient;
import psp.payment.qrcode.clients.StoreClient;
import psp.payment.qrcode.dtos.*;
import psp.payment.qrcode.exceptions.*;
import psp.payment.qrcode.service.QRService;
import psp.payment.qrcode.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("qr")
@Slf4j
public class QRCodeController {

    private final AuthClient authClient;
    private final StoreClient storeClient;
    private final LoggerUtil loggerUtil;
    private final QRService qrService;

    @Autowired
    public QRCodeController(AuthClient authClient, StoreClient storeClient, LoggerUtil loggerUtil, QRService qrService) {
        this.authClient = authClient;
        this.storeClient = storeClient;
        this.loggerUtil = loggerUtil;
        this.qrService = qrService;
    }

    @GetMapping("/code/{requestId}")
    public ResponseEntity<?> getPaymentQRCode(@PathVariable long requestId){
        try {
            return ResponseEntity.ok(new QRCode(qrService.generateQR(requestId)));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        } catch (QRCodeNotGeneratedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> isQREnabled(@PathVariable long requestId){
        try {
            PaymentRequest request = qrService.getByRequestId(requestId);
            if (!qrService.paymentEnabledForRequest(request)) {
                return ResponseEntity.badRequest().body("Request has already been processed");
            }
            if (!request.getBillingCycle().equals("ONE_TIME")) {
                return ResponseEntity.badRequest().body("Subscriptions not supported");
            }
            return ResponseEntity.ok(qrService.getByStoreId(request));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Request not found.");
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<?> isQREnabledForStore(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to check QR payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to check QR payment subscription"));
            throw new UnauthorizedException("You don't have a permission to check QR payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            return ResponseEntity.ok(qrService.getByStoreId(storeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @PostMapping("/enable/{storeId}")
    public ResponseEntity<?> enable(HttpServletRequest request, @RequestBody @Valid MerchantInfoDTO dto, @RequestHeader("Authorization") String token) throws UnauthorizedException, UnauthenticatedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to create QR payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to create QR payment subscription"));
            throw new UnauthorizedException("You don't have a permission to create QR payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            qrService.enable(request, dto, storeId);
            return ResponseEntity.ok(new IsEnabledDTO(true));
        } catch (MerchantCredentialsNotValidException e) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }
    }

    @GetMapping("/disable/{storeId}")
    public ResponseEntity<?> disable(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to delete QR payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to delete QR payment subscription"));
            throw new UnauthorizedException("You don't have a permission to delete QR payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        try {
            qrService.disable(request, storeId);
            return ResponseEntity.ok(new IsEnabledDTO(false));
        } catch (StoreNotFoundException e) {
            return ResponseEntity.badRequest().body("Store not found.");
        }
    }

    @GetMapping("/invoice/{requestId}")
    public ResponseEntity<?> getInvoiceResponse(HttpServletRequest request, @PathVariable long requestId){
        try {
            return ResponseEntity.ok(qrService.getInvoiceResponse(request, requestId));
        } catch (RequestNotFoundException | StoreNotFoundException | InvoiceNotValidException e) {
            log.info(loggerUtil.getLogMessage(request, "Invoice for request (id=" + request + ") not created. Error: " + e.getMessage()));
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("bank-payment-response")
    public void setPaymentOutcome(@RequestBody PaymentResponseDTO dto){
        qrService.setPaymentOutcome(dto);
    }
}
