package psp.payment.bitcoin.service;

import psp.payment.bitcoin.model.Transaction;

import javax.servlet.http.HttpServletRequest;

public interface TransactionService {

    Transaction save(HttpServletRequest request, long requestId, long merchantOrderId, String status);

    boolean exists(long requestId);
}
