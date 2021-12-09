package psp.request.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.request.clients.StoreClient;
import psp.request.dtos.PaymentRequestDTO;
import psp.request.dtos.PaymentResponseDTO;
import psp.request.exceptions.NotFoundException;
import psp.request.model.PaymentRequest;
import psp.request.repositories.PaymentRequestRepository;

@Service
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository repository;
    @Autowired
    private StoreClient storeClient;

    public PaymentResponseDTO create(PaymentRequestDTO dto) throws NotFoundException {
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
        return new PaymentResponseDTO(request.getId());
    }
}
