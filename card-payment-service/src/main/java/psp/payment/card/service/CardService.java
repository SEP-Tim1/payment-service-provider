package psp.payment.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.card.client.Client;
import psp.payment.card.dtos.PaymentRequest;
import psp.payment.card.exceptions.StoreNotFoundException;
import psp.payment.card.model.Card;
import psp.payment.card.repositories.CardRepository;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final Client client;

    @Autowired
    public CardService(CardRepository cardRepository, Client client) {
        this.cardRepository = cardRepository;
        this.client = client;
    }

    public Card getByStoreId(PaymentRequest request) throws StoreNotFoundException {
        Card card = cardRepository.findByStoreId(request.getStoreId());
        if(card != null) return card;
        throw new StoreNotFoundException();
    }

    public PaymentRequest getByRequestId(long id){
        return client.getById(id);
    }
}
