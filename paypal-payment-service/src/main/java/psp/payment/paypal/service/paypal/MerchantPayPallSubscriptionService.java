package psp.payment.paypal.service.paypal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.paypal.dto.MerchantSubscriptionDTO;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.exceptions.NotUniqueException;
import psp.payment.paypal.model.MerchantSubscription;
import psp.payment.paypal.repository.MerchantSubscriptionRepository;
import psp.payment.paypal.service.MerchantSubscriptionService;
import psp.payment.paypal.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class MerchantPayPallSubscriptionService implements MerchantSubscriptionService {

    @Autowired
    private MerchantSubscriptionRepository repository;
    @Autowired
    private LoggerUtil loggerUtil;

    @Override
    public void create(HttpServletRequest request, MerchantSubscriptionDTO dto, long storeId) throws NotUniqueException {
        MerchantSubscription subscription = repository.findByStoreId(storeId);
        if (subscription != null) {
            log.warn(loggerUtil.getLogMessage(request, "Subscription creation attempt for store (id=" + storeId + "). It already exists"));
            throw new NotUniqueException("Subscription already exists");
        }
        subscription = new MerchantSubscription(
                storeId,
                dto.getClientId(),
                dto.getClientSecret()
        );
        subscription = repository.save(subscription);
        log.info(loggerUtil.getLogMessage(request, "Merchant subscription (id=" + subscription.getId() + ") created for store (id=" + storeId + ")"));
    }

    @Override
    public void delete(HttpServletRequest request, long storeId) throws NotFoundException {
        MerchantSubscription subscription = repository.findByStoreId(storeId);
        if (subscription == null) {
            log.warn(loggerUtil.getLogMessage(request, "Subscription deletion attempt for store (id=" + storeId + "). It does not exist"));
            throw new NotFoundException("Subscription not found");
        }
        repository.delete(subscription);
        log.info(loggerUtil.getLogMessage(request, "Merchant subscription for store (id=" + storeId + ") deketed"));
    }

    @Override
    public boolean isSubscribed(long storeId) {
        MerchantSubscription subscription = repository.findByStoreId(storeId);
        if (subscription == null) {
            return false;
        }
        return true;
    }

    @Override
    public MerchantSubscriptionDTO get(long storeId) {
        MerchantSubscription subscription = repository.findByStoreId(storeId);
        if (subscription == null) {
            return null;
        }
        return new MerchantSubscriptionDTO(
                subscription.getClientId(),
                subscription.getClientSecret()
        );
    }
}
