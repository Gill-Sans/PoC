package com.capgemini.dllpoc.api.delhaize.application.address;

import com.capgemini.dllpoc.api.delhaize.model.Address;
import com.capgemini.dllpoc.api.delhaize.port.in.address.AddressUseCase;
import com.capgemini.dllpoc.api.delhaize.port.out.address.AddressRepositoryPort;
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
public class AddressService implements AddressUseCase {

    private final AddressRepositoryPort repository;

    @Override
    public Optional<Address> findById(@NotNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Address> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Address save(@NotNull @Valid Address address) {
        return repository.save(address);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }
}

