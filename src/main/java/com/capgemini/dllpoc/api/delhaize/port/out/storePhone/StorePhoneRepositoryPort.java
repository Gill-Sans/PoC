package com.capgemini.dllpoc.api.delhaize.port.out.storePhone;

import com.capgemini.dllpoc.api.delhaize.model.StorePhone;

import java.util.List;
import java.util.Optional;

public interface StorePhoneRepositoryPort {
    Optional<StorePhone> findById(Long id);
    List<StorePhone> findAll();
    StorePhone save(StorePhone storePhone);
    void deleteById(Long id);
}
