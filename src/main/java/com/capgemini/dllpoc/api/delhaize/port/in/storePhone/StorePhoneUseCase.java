package com.capgemini.dllpoc.api.delhaize.port.in.storePhone;

import com.capgemini.dllpoc.api.delhaize.model.StorePhone;

import java.util.List;
import java.util.Optional;

public interface StorePhoneUseCase {
    Optional<StorePhone> findById(Long id);
    List<StorePhone> findAll();
    StorePhone save(StorePhone storePhone);
    void deleteById(Long id);
}

