package psp.request.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.request.clients.StoreClient;
import psp.request.dtos.PaymentRequestDTO;
import psp.request.dtos.PaymentResponseDTO;
import psp.request.exceptions.NotFoundException;
import psp.request.model.PaymentRequest;
import psp.request.repositories.PaymentRequestRepository;

import java.util.Optional;

@Slf4j
@Service
public class PaymentRequestService {

    private final PaymentRequestRepository repository;
    private final StoreClient storeClient;

    @Autowired
    public PaymentRequestService(PaymentRequestRepository repository, StoreClient storeClient) {
        this.repository = repository;
        this.storeClient = storeClient;
    }

    public PaymentResponseDTO create(PaymentRequestDTO dto) throws NotFoundException {
        long storeId;
        try {
            storeId = storeClient.getIdByApiToken(dto.getApiToken());
        } catch (Exception e) {
            log.warn("Attempt to create a payment request with invalid API Token");
            throw new NotFoundException("Store could not be found");
        }
        PaymentRequest request = new PaymentRequest(
                storeId,
                dto.getMerchantOrderId(),
                dto.getMerchantTimestamp(),
                dto.getAmount(),
                dto.getSuccessUrl(),
                dto.getFailureUrl(),
                dto.getErrorUrl()
        );
        request = repository.save(request);
        log.info("Payment request (id=" + request.getId() + ") created");
        return new PaymentResponseDTO(request.getId());
    }

    public PaymentRequest getById(long id) throws NotFoundException {
        if(repository.findById(id).isPresent()) return repository.findById(id).get();
        throw new NotFoundException("Payment Request could not be found");
    }


    public String getFailureUrl(long requestId) throws NotFoundException {
        Optional<PaymentRequest> request = repository.findById(requestId);
        if(request.isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        return request.get().getFailureUrl();
    }
}
