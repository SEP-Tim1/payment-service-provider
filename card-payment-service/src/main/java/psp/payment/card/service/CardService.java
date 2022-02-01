package psp.payment.card.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import psp.payment.card.client.Bank1Client;
import psp.payment.card.client.Bank2Client;
import psp.payment.card.client.Client;
import psp.payment.card.dtos.*;
import psp.payment.card.exceptions.InvoiceNotValidException;
import psp.payment.card.exceptions.MerchantCredentialsNotValidException;
import psp.payment.card.exceptions.RequestNotFoundException;
import psp.payment.card.exceptions.StoreNotFoundException;
import psp.payment.card.model.Card;
import psp.payment.card.model.Transaction;
import psp.payment.card.repositories.CardRepository;
import psp.payment.card.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final Client client;
    private final Bank1Client bank1;
    private final Bank2Client bank2;
    private final TransactionService transactionService;
    private DiscoveryClient discoveryClient;
    @Value("${service.gateway.name}")
    private String gatewayName;
    private LoggerUtil loggerUtil;

    @Autowired
    public CardService(CardRepository cardRepository, Client client, Bank1Client bank1, Bank2Client bank2, TransactionService transactionService, DiscoveryClient discoveryClient, LoggerUtil loggerUtil) {
        this.cardRepository = cardRepository;
        this.client = client;
        this.bank1 = bank1;
        this.bank2 = bank2;
        this.transactionService = transactionService;
        this.discoveryClient = discoveryClient;
        this.loggerUtil = loggerUtil;
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

    public PaymentRequest getByRequestId(long id){
        return client.getById(id);
    }

    public boolean paymentEnabledForRequest(PaymentRequest request) {
        if (client.isProcessed(request.getId())) {
            return false;
        }
        return true;
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

    public void disable(HttpServletRequest r, long storeId) throws StoreNotFoundException {
        Card card = getByStoreId(storeId);
        card.setCardPaymentEnabled(false);
        cardRepository.save(card);
        log.info(loggerUtil.getLogMessage(r, "Store (id=" + storeId + ") disabled card payment services"));
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
}
