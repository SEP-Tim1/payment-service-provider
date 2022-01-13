package psp.payment.bitcoin.service;

import psp.payment.bitcoin.model.Transaction;

public interface TransactionService {

    Transaction save(long requestId, long merchantOrderId, String status);

    boolean exists(long requestId);
}
