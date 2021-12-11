package psp.payment.card.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.card.client.Bank1Client;
import psp.payment.card.client.Bank2Client;
import psp.payment.card.client.Client;
import psp.payment.card.dtos.MerchantCredentialsDTO;
import psp.payment.card.dtos.MerchantInfoDTO;
import psp.payment.card.dtos.PaymentRequest;
import psp.payment.card.exceptions.MerchantCredentialsNotValidException;
import psp.payment.card.exceptions.StoreNotFoundException;
import psp.payment.card.model.Card;
import psp.payment.card.repositories.CardRepository;

@Slf4j
@Service
public class CardService {

    private final CardRepository cardRepository;

    private final Client client;
    private final Bank1Client bank1;
    private final Bank2Client bank2;

    @Autowired
    public CardService(CardRepository cardRepository, Client client, Bank1Client bank1, Bank2Client bank2) {
        this.cardRepository = cardRepository;
        this.client = client;
        this.bank1 = bank1;
        this.bank2 = bank2;
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

    public void enable(MerchantInfoDTO dto, long storeId) throws StoreNotFoundException, MerchantCredentialsNotValidException {
        validateCredentials(dto, storeId);
        Card card = getByStoreId(storeId);
        card.setCardPaymentEnabled(true);
        cardRepository.save(card);
        log.info("Store (id=" + storeId + ") enabled card payment services");
    }

    public void disable(long storeId) throws StoreNotFoundException {
        Card card = getByStoreId(storeId);
        card.setCardPaymentEnabled(false);
        cardRepository.save(card);
        log.info("Store (id=" + storeId + ") disabled card payment services");
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
