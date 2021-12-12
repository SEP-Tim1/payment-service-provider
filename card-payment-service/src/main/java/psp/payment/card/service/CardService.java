package psp.payment.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.card.client.Bank1Client;
import psp.payment.card.client.Bank2Client;
import psp.payment.card.client.Client;
import psp.payment.card.client.WebShopClient;
import psp.payment.card.dtos.*;
import psp.payment.card.exceptions.InvoiceNotValidException;
import psp.payment.card.exceptions.MerchantCredentialsNotValidException;
import psp.payment.card.exceptions.RequestNotFoundException;
import psp.payment.card.exceptions.StoreNotFoundException;
import psp.payment.card.model.Card;
import psp.payment.card.repositories.CardRepository;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final Client client;
    private final Bank1Client bank1;
    private final Bank2Client bank2;
    private final WebShopClient webShopClient;

    @Autowired
    public CardService(CardRepository cardRepository, Client client, Bank1Client bank1, Bank2Client bank2, WebShopClient webShopClient) {
        this.cardRepository = cardRepository;
        this.client = client;
        this.bank1 = bank1;
        this.bank2 = bank2;

        this.webShopClient = webShopClient;
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
        validateCredentials(dto);
        try {
            Card card = getByStoreId(storeId);
            card.update(true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        } catch (StoreNotFoundException e) {
            Card card = new Card(storeId, true, dto.getMid(), dto.getMpassword(), dto.getBank());
            cardRepository.save(card);
        }
    }

    public void disable(long storeId) throws StoreNotFoundException {
        Card card = getByStoreId(storeId);
        card.setCardPaymentEnabled(false);
        cardRepository.save(card);
    }


    public InvoiceResponseDTO getInvoiceResponse(long requestId) throws StoreNotFoundException, InvoiceNotValidException, RequestNotFoundException {
        try {
            PaymentRequest request = getByRequestId(requestId);
            Card card = getByStoreId(request.getStoreId());
            return sendInvoice(new InvoiceDTO(request, card), card.getBank());
        } catch (StoreNotFoundException se) {
            throw new StoreNotFoundException();
        } catch (InvoiceNotValidException ie) {
            throw new InvoiceNotValidException();
        } catch (Exception e) {
            throw new RequestNotFoundException();
        }
    }

    public void sendPaymentResponseToWebShop(PaymentResponseDTO dto){
        try {
            webShopClient.bankPaymentResponse(dto);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private InvoiceResponseDTO sendInvoice(InvoiceDTO invoice, int bank) throws InvoiceNotValidException {
        try{
            if (bank == 1) return bank1.generate(invoice);
            return bank2.generate(invoice);
        } catch (Exception e){
            throw new InvoiceNotValidException();
        }
    }

    private void validateCredentials(MerchantInfoDTO dto) throws MerchantCredentialsNotValidException {
        try {
            MerchantCredentialsDTO credentials = new MerchantCredentialsDTO(dto.getMid(), dto.getMpassword());
            if (dto.getBank() == 1) {
                bank1.validate(credentials);
            } else {
                bank2.validate(credentials);
            }
        } catch (Exception e) {
            throw new MerchantCredentialsNotValidException();
        }
    }
}
