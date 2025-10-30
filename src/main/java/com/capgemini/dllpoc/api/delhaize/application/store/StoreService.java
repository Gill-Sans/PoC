package com.capgemini.dllpoc.api.delhaize.application.store;

import com.capgemini.dllpoc.api.delhaize.model.Store;
import com.capgemini.dllpoc.api.delhaize.port.in.store.StoreUseCase;
import com.capgemini.dllpoc.api.delhaize.port.out.store.StoreRepositoryPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {
    private final StoreRepositoryPort storeRepository;

    @Override
    public Optional<Store> findById(@NotNull Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Optional<Store> findByStoreNumber(@NotNull Long storeNumber) {
        return storeRepository.findByStoreNumber(storeNumber);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    @Transactional
    public Store save(@NotNull @Valid Store store) {
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        storeRepository.deleteById(id);
    }
}
