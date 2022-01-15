package psp.payment.paypal.service;

import psp.payment.paypal.dto.MerchantSubscriptionDTO;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.exceptions.NotUniqueException;

public interface MerchantSubscriptionService {

    void create(MerchantSubscriptionDTO dto, long storeId) throws NotUniqueException;

    void delete(long storeId) throws NotFoundException;

    boolean isSubscribed(long storeId);
}
