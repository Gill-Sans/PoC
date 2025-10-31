package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.address;

import com.capgemini.dllpoc.api.delhaize.model.Address;
import com.capgemini.dllpoc.api.delhaize.port.out.address.AddressRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryAdapter implements AddressRepositoryPort {

    private final JpaAddressRepository jpaRepository;

    @Override
    public Optional<Address> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Address> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    @Transactional
    public Address save(Address address) {
        return jpaRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
