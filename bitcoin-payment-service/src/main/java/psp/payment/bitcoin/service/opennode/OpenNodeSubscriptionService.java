package psp.payment.bitcoin.service.opennode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.model.Subscription;
import psp.payment.bitcoin.repository.SubscriptionRepository;
import psp.payment.bitcoin.service.SubscriptionService;

import java.util.Optional;

@Service
@Slf4j
public class OpenNodeSubscriptionService implements SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;

    @Override
    public void create(long storeId, String apiKey) {
        if (repository.findByStoreId(storeId).isPresent()) {
            log.info("Subscription creation attempt for store (id=" + storeId + "). Subscription already exists.");
            return;
        }
        Subscription subscription = new Subscription(storeId, apiKey);
        subscription = repository.save(subscription);
        log.info("Subscription (id=" + subscription.getId() + ") created for a store (id=" + storeId + ")");
    }

    @Override
    public void delete(long storeId) throws NotFoundException {
        Optional<Subscription> subOpt = repository.findByStoreId(storeId);
        if (subOpt.isEmpty()) {
            log.info("Subscription deletion attempt for store (id= )" + storeId + "). Subscription does not exist.");
            throw new NotFoundException("Subscription not found");
        }
        repository.delete(subOpt.get());
        log.info("Subscription deleted for a store (id=" + storeId + ")");
    }

    @Override
    public boolean exists(long storeId) {
        return repository.findByStoreId(storeId).isPresent();
    }

    @Override
    public String getApiKey(long storeId) throws NotFoundException {
        if (!exists(storeId)) {
            throw new NotFoundException("Store not found");
        }
        return repository.findByStoreId(storeId).get().getApiKey();
    }
}
