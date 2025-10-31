package com.capgemini.dllpoc.api.delhaize.port.in.address;

import com.capgemini.dllpoc.api.delhaize.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressUseCase {
    Optional<Address> findById(Long id);
    List<Address> findAll();
    Address save(Address address);
    void deleteById(Long id);
}

