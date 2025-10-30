package com.capgemini.dllpoc.api.delhaize.port.out;

import com.capgemini.dllpoc.api.delhaize.model.Store;
import java.util.List;
import java.util.Optional;

public interface StoreRepositoryPort {
    Optional<Store> findById(Long id);
    Optional<Store> findByStoreNumber(Long storeNumber);
    List<Store> findAll();
    Store save(Store store);
    void deleteById(Long id);
}