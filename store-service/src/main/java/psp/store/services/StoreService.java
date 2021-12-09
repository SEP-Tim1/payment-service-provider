package psp.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.store.exceptions.NotFoundException;
import psp.store.model.Store;
import psp.store.repositories.StoreRepository;

import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository repository;
    @Autowired
    private TokenService tokenService;

    public void create(String name, long userId) {
        Store store = new Store(name, userId);
        store = repository.save(store);
        String apiToken = tokenService.generateToken(store);
        store.setApiToken(apiToken);
        repository.save(store);
    }

    public Store getByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    public long getIdByApiToken(String apiToken) throws NotFoundException {
        Optional<Store> store = repository.getByApiToken(apiToken);
        if(store.isEmpty()) {
            throw new NotFoundException("Store not found");
        }
        return store.get().getId();
    }
}
