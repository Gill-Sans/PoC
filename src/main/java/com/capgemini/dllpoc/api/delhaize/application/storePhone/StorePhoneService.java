package com.capgemini.dllpoc.api.delhaize.application.storePhone;

import com.capgemini.dllpoc.api.delhaize.model.StorePhone;
import com.capgemini.dllpoc.api.delhaize.port.in.storePhone.StorePhoneUseCase;
import com.capgemini.dllpoc.api.delhaize.port.out.storePhone.StorePhoneRepositoryPort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class StorePhoneService implements StorePhoneUseCase {

    private final StorePhoneRepositoryPort repository;

    @Override
    public Optional<StorePhone> findById(@NotNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<StorePhone> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public StorePhone save(@NotNull @Valid StorePhone storePhone) {
        return repository.save(storePhone);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }
}
