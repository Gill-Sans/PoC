package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa;

import com.capgemini.dllpoc.api.delhaize.model.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreNumber(Long storeNumber);
}