package psp.payment.paypal.service;

import psp.payment.paypal.dto.MerchantSubscriptionDTO;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.exceptions.NotUniqueException;

import javax.servlet.http.HttpServletRequest;

public interface MerchantSubscriptionService {

    void create(HttpServletRequest request, MerchantSubscriptionDTO dto, long storeId) throws NotUniqueException;

    void delete(HttpServletRequest request, long storeId) throws NotFoundException;

    boolean isSubscribed(long storeId);

    MerchantSubscriptionDTO get(long storeId);
}
