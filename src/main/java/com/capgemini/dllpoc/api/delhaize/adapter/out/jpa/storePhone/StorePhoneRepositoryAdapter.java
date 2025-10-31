package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.storePhone;

import com.capgemini.dllpoc.api.delhaize.model.StorePhone;
import com.capgemini.dllpoc.api.delhaize.port.out.storePhone.StorePhoneRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StorePhoneRepositoryAdapter implements StorePhoneRepositoryPort {

    private final JpaStorePhoneRepository jpaRepository;

    @Override
    public Optional<StorePhone> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<StorePhone> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    @Transactional
    public StorePhone save(StorePhone storePhone) {
        return jpaRepository.save(storePhone);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

