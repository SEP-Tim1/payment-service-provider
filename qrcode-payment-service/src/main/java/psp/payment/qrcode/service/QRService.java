package psp.payment.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import psp.payment.qrcode.clients.Bank1Client;
import psp.payment.qrcode.clients.Bank2Client;
import psp.payment.qrcode.clients.Client;
import psp.payment.qrcode.dtos.*;
import psp.payment.qrcode.exceptions.*;
import psp.payment.qrcode.model.Card;
import psp.payment.qrcode.model.Currency;
import psp.payment.qrcode.model.Transaction;
import psp.payment.qrcode.repositories.CardRepository;
import psp.payment.qrcode.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class QRService {

    private final Client client;
    private final CardRepository cardRepository;
    private final TransactionService transactionService;
    private final LoggerUtil loggerUtil;
    private DiscoveryClient discoveryClient;
    private final Bank1Client bank1;
    private final Bank2Client bank2;

    @Value("${service.gateway.name}")
    private String gatewayName;

    public QRService(Client client, CardRepository cardRepository, TransactionService transactionService, LoggerUtil loggerUtil, Bank1Client bank1, Bank2Client bank2, DiscoveryClient discoveryClient) {
        this.client = client;
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
        this.loggerUtil = loggerUtil;
        this.discoveryClient = discoveryClient;
        this.bank1 = bank1;
        this.bank2 = bank2;
    }

    public PaymentRequest getByRequestId(long id) {
        return client.getById(id);
    }

    public boolean paymentEnabledForRequest(PaymentRequest request) {
        if (client.isProcessed(request.getId())) {
            return false;
        }
        return true;
    }

    public Card getByStoreId(PaymentRequest request) throws StoreNotFoundException {
        Card card = cardRepository.findByStoreId(request.getStoreId());
        if(card != null) {
            return card;
        }
        throw new StoreNotFoundException();
    }

    public Card getByStoreId(long id) throws StoreNotFoundException {
        Card card = cardRepository.findByStoreId(id);
        if(card != null) {
            return card;
        }
        throw new StoreNotFoundException();
    }

    public void enable(HttpServletRequest r, MerchantInfoDTO dto, long storeId) throws MerchantCredentialsNotValidException {
        validateCredentials(r, dto, storeId);
        try {
            Card card = getByStoreId(storeId);
            card.update(true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        } catch (StoreNotFoundException e) {
            Card card = new Card(storeId, true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        }
        log.info(loggerUtil.getLogMessage(r, "Store (id=" + storeId + ") enabled card payment services"));

    }

    private void validateCredentials(HttpServletRequest r, MerchantInfoDTO dto, long storeId) throws MerchantCredentialsNotValidException {
        try {
            MerchantCredentialsDTO credentials = new MerchantCredentialsDTO(dto.getMid(), dto.getMpassword());
            if (dto.getBank() == 1) {
                bank1.validate(credentials);
            } else {
                bank2.validate(credentials);
            }
        } catch (Exception e) {
            log.info(loggerUtil.getLogMessage(r, "Store (id=" + storeId + ") has invalid merchant id and/or merchant password"));
            throw new MerchantCredentialsNotValidException();
        }
    }

    public void disable(HttpServletRequest r, long storeId) throws StoreNotFoundException {
        Card card = getByStoreId(storeId);
        card.setQrPaymentEnabled(false);
        cardRepository.save(card);
        log.info(loggerUtil.getLogMessage(r, "Store (id=" + storeId + ") disabled QR payment services"));
    }

    public InvoiceResponseDTO getInvoiceResponse(HttpServletRequest r, long requestId) throws StoreNotFoundException, InvoiceNotValidException, RequestNotFoundException {
        try {
            PaymentRequest request = getByRequestId(requestId);
            Card card = getByStoreId(request.getStoreId());
            List<ServiceInstance> gateway = discoveryClient.getInstances(gatewayName);
            String host = gateway.get(0).getHost();
            System.out.println(host);
            int port = gateway.get(0).getPort();
            String callbackUrl = "https://" + host + ":" + port + "/card/card/bank-payment-response";
            InvoiceResponseDTO response = sendInvoice(r, new InvoiceDTO(request, card, callbackUrl), card.getBank());
            log.info(loggerUtil.getLogMessage(r, "Invoice for request (id=" + requestId + ") created. Payment (id=" + response.getPaymentId() + ", url=" + response.getPaymentUrl() + ") received"));
            return response;
        } catch (StoreNotFoundException se) {
            throw new StoreNotFoundException();
        } catch (InvoiceNotValidException ie) {
            throw new InvoiceNotValidException();
        } catch (Exception e) {
            throw new RequestNotFoundException();
        }
    }

    private InvoiceResponseDTO sendInvoice(HttpServletRequest r, InvoiceDTO invoice, int bank) throws InvoiceNotValidException {
        try{
            InvoiceResponseDTO response;
            if (bank == 1) {
                response = bank1.generate(invoice);
                log.info(loggerUtil.getLogMessage(r, "Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                        + ", requestId=" + invoice.getRequestId()
                        + ") sent to bank1"));
            } else {
                response = bank2.generate(invoice);
                log.info(loggerUtil.getLogMessage(r, "Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                        + ", requestId=" + invoice.getRequestId()
                        + ") sent to bank2"));
            }
            log.info(loggerUtil.getLogMessage(r, "Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                    + ", requestId=" + invoice.getRequestId()
                    + ") received paymentId=" + response.getPaymentId()));
            return response;
        } catch (Exception e){
            throw new InvoiceNotValidException();
        }
    }

    public void setPaymentOutcome(PaymentResponseDTO dto){
        try {
            Transaction transaction = transactionService.save(dto);
            if (transaction == null) {
                return;
            }
            client.setPaymentRequestOutcome(transaction.getRequestId(), new PaymentOutcomeDTO(
                    PaymentStatusDTO.valueOf(transaction.getTransactionStatus()),
                    transaction.getErrorMessage()));
        } catch(Exception e) {
            return;
        }
    }

    public String generateQR(long requestId) throws StoreNotFoundException, QRCodeNotGeneratedException {
        PaymentRequest request = getByRequestId(requestId);
        Card card = getByStoreId(request);
        String path = new File("src/main/resources/qrs").getAbsolutePath();
        String filePath = path + "/qr" + request.getId() + LocalDateTime.now() + ".png";
        try {
            generateQRCodeImage(QRText(card, request.getAmount(), request.getCurrency()), 300, 300, filePath);
            return filePath;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            throw new QRCodeNotGeneratedException();
        }
    }

    public String QRText(Card c, float amount, Currency currency){
        return c.getName() + ';' + c.getAccountNumber() + ';' + amount + ';' + currency;
    }


    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
