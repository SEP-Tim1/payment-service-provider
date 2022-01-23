package psp.payment.bitcoin.service;

import psp.payment.bitcoin.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface SubscriptionService {

    void create(HttpServletRequest request, long storeId, String apiKey);

    void delete(HttpServletRequest request, long storeId) throws NotFoundException;

    boolean exists(long storeId);

    String getApiKey(long storeId) throws NotFoundException;
}
