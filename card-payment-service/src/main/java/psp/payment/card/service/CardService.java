package psp.payment.card.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import psp.payment.card.repositories.CardRepository;

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

    @Autowired
    public CardService(CardRepository cardRepository, Client client, Bank1Client bank1, Bank2Client bank2, TransactionService transactionService, DiscoveryClient discoveryClient) {
        this.cardRepository = cardRepository;
        this.client = client;
        this.bank1 = bank1;
        this.bank2 = bank2;
        this.transactionService = transactionService;
        this.discoveryClient = discoveryClient;
    }

    public Card getByStoreId(PaymentRequest request) throws StoreNotFoundException {
        Card card = cardRepository.findByStoreId(request.getStoreId());
        if(card != null) return card;
        throw new StoreNotFoundException();
    }

    public Card getByStoreId(long id) throws StoreNotFoundException {
        Card card = cardRepository.findByStoreId(id);
        if(card != null) return card;
        throw new StoreNotFoundException();
    }

    public PaymentRequest getByRequestId(long id){
        return client.getById(id);
    }

    public void enable(MerchantInfoDTO dto, long storeId) throws MerchantCredentialsNotValidException {
        validateCredentials(dto, storeId);
        try {
            Card card = getByStoreId(storeId);
            card.update(true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        } catch (StoreNotFoundException e) {
            Card card = new Card(storeId, true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        }
        log.info("Store (id=" + storeId + ") enabled card payment services");
    }

    public void disable(long storeId) throws StoreNotFoundException {
        Card card = getByStoreId(storeId);
        card.setCardPaymentEnabled(false);
        cardRepository.save(card);
        log.info("Store (id=" + storeId + ") disabled card payment services");
    }

    public InvoiceResponseDTO getInvoiceResponse(long requestId) throws StoreNotFoundException, InvoiceNotValidException, RequestNotFoundException {
        try {
            PaymentRequest request = getByRequestId(requestId);
            Card card = getByStoreId(request.getStoreId());
            List<ServiceInstance> gateway = discoveryClient.getInstances("gateway");
            String host = gateway.get(0).getHost();
            int port = gateway.get(0).getPort();
            String callbackUrl = "http://" + host + ":" + port + "/card/card/bank-payment-response";
            return sendInvoice(new InvoiceDTO(request, card, callbackUrl), card.getBank());
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
            transactionService.save(dto);
            client.setPaymentRequestOutcome(dto.getRequestId(), new PaymentOutcomeDTO(
                    PaymentStatusDTO.valueOf(dto.getTransactionStatus()),
                    dto.getErrorMessage()));
        } catch(Exception e) {
            return;
        }
    }

    private InvoiceResponseDTO sendInvoice(InvoiceDTO invoice, int bank) throws InvoiceNotValidException {
        try{
            InvoiceResponseDTO response;
            if (bank == 1) {
                response = bank1.generate(invoice);
                log.info("Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                        + ", requestId=" + invoice.getRequestId()
                        + ") sent to bank1");
            } else {
                response = bank2.generate(invoice);
                log.info("Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                        + ", requestId=" + invoice.getRequestId()
                        + ") sent to bank2");
            }
            log.info("Invoice (merchantOrderId=" + invoice.getMerchantOrderId()
                    + ", requestId=" + invoice.getRequestId()
                    + ") received paymentId=" + response.getPaymentId());
            return response;
        } catch (Exception e){
            throw new InvoiceNotValidException();
        }
    }

    private void validateCredentials(MerchantInfoDTO dto, long storeId) throws MerchantCredentialsNotValidException {
        try {
            MerchantCredentialsDTO credentials = new MerchantCredentialsDTO(dto.getMid(), dto.getMpassword());
            if (dto.getBank() == 1) {
                bank1.validate(credentials);
            } else {
                bank2.validate(credentials);
            }
        } catch (Exception e) {
            log.info("Store (id=" + storeId + ") has invalid merchant id and/or merchant password");
            throw new MerchantCredentialsNotValidException();
        }
    }
}
