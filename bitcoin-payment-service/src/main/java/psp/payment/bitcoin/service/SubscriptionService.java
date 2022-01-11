package psp.payment.bitcoin.service;

import psp.payment.bitcoin.exceptions.NotFoundException;

public interface SubscriptionService {

    void create(long storeId, String apiKey);
    void delete(long storeId) throws NotFoundException;
    boolean exists(long storeId);
}
