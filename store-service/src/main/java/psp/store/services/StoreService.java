package psp.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.store.model.Store;
import psp.store.repositories.StoreRepository;

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
}
