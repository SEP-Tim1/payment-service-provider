package psp.request.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.request.clients.StoreClient;
import psp.request.dtos.PaymentRequestDTO;
import psp.request.dtos.PaymentResponseDTO;
import psp.request.dtos.PaymentResponseIdDTO;
import psp.request.exceptions.NotFoundException;
import psp.request.model.PaymentRequest;
import psp.request.repositories.PaymentRequestRepository;

import java.util.Optional;

@Service
public class PaymentRequestService {

    private final PaymentRequestRepository repository;
    private final StoreClient storeClient;

    @Autowired
    public PaymentRequestService(PaymentRequestRepository repository, StoreClient storeClient) {
        this.repository = repository;
        this.storeClient = storeClient;
    }

    public PaymentResponseIdDTO create(PaymentRequestDTO dto) throws NotFoundException {
        long storeId;
        try {
            storeId = storeClient.getIdByApiToken(dto.getApiToken());
        } catch (Exception e) {
            e.printStackTrace();
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
        return new PaymentResponseIdDTO(request.getId());
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

    public void setFlags(PaymentResponseDTO dto) throws NotFoundException {
        if(repository.findById(dto.getRequestId()).isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        PaymentRequest request = repository.findById(dto.getRequestId()).get();
        request.setProcessed(true);
        request.setSuccessful(dto.getTransactionStatus().equals("SUCCESS"));
        repository.save(request);
    }
}
