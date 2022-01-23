package psp.store.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.store.exceptions.NotFoundException;
import psp.store.model.Store;
import psp.store.repositories.StoreRepository;
import psp.store.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Service
public class StoreService {

    @Autowired
    private StoreRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoggerUtil loggerUtil;

    public void create(HttpServletRequest request, String name, long userId) {
        Store store = new Store(name, userId);
        store = repository.save(store);
        String apiToken = tokenService.generateToken(request, store);
        store.setApiToken(apiToken);
        store = repository.save(store);
        log.info(loggerUtil.getLogMessage(request, "Store (id=" + store.getId() + ") created"));
    }

    public Store getByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    public long getIdByApiToken(HttpServletRequest request, String apiToken) throws NotFoundException {
        Optional<Store> store = repository.getByApiToken(apiToken);
        if(store.isEmpty()) {
            log.warn(loggerUtil.getLogMessage(request, "Store extraction attempt from an invalid API Token"));
            throw new NotFoundException("Store not found");
        }
        return store.get().getId();
    }

    public long getIdByUserId(HttpServletRequest request, long userId) throws NotFoundException {
        Optional<Store> store = repository.getByUserId(userId);
        if(store.isEmpty()) {
            log.warn(loggerUtil.getLogMessage(request, "Store extraction attempt from an invalid user id"));
            throw new NotFoundException("Store not found");
        }
        return store.get().getId();
    }

    public String getApiTokenById(HttpServletRequest request, long id) throws NotFoundException {
        Optional<Store> store = repository.findById(id);
        if(store.isEmpty()) {
            log.warn(loggerUtil.getLogMessage(request, "API token extraction attempt from an invalid store id"));
            throw new NotFoundException("Store not found");
        }
        return store.get().getApiToken();
    }
}
