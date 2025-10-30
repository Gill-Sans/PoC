package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa;

import com.capgemini.dllpoc.api.delhaize.model.Store;
import com.capgemini.dllpoc.api.delhaize.port.out.StoreRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryAdapter implements StoreRepositoryPort {
    private final JpaStoreRepository jpa;

    public Optional<Store> findById(Long id) {
        return jpa.findById(id);
    }

    public Optional<Store> findByStoreNumber(Long storeNumber) {
        return jpa.findByStoreNumber(storeNumber);
    }

    public List<Store> findAll() {
        return jpa.findAll();
    }

    @Transactional
    public Store save(Store store) {
        return jpa.save(store);
    }

    @Transactional
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}