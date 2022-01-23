package psp.payment.bitcoin.service.opennode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.model.Subscription;
import psp.payment.bitcoin.repository.SubscriptionRepository;
import psp.payment.bitcoin.service.SubscriptionService;
import psp.payment.bitcoin.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Slf4j
public class OpenNodeSubscriptionService implements SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;
    @Autowired
    private LoggerUtil loggerUtil;

    @Override
    public void create(HttpServletRequest request, long storeId, String apiKey) {
        if (repository.findByStoreId(storeId).isPresent()) {
            log.info(loggerUtil.getLogMessage(request, "Subscription creation attempt for store (id=" + storeId + "). Subscription already exists."));
            return;
        }
        Subscription subscription = new Subscription(storeId, apiKey);
        subscription = repository.save(subscription);
        log.info(loggerUtil.getLogMessage(request, "Subscription (id=" + subscription.getId() + ") created for a store (id=" + storeId + ")"));
    }

    @Override
    public void delete(HttpServletRequest request, long storeId) throws NotFoundException {
        Optional<Subscription> subOpt = repository.findByStoreId(storeId);
        if (subOpt.isEmpty()) {
            log.info(loggerUtil.getLogMessage(request, "Subscription deletion attempt for store (id= )" + storeId + "). Subscription does not exist."));
            throw new NotFoundException("Subscription not found");
        }
        repository.delete(subOpt.get());
        log.info(loggerUtil.getLogMessage(request, "Subscription deleted for a store (id=" + storeId + ")"));
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
