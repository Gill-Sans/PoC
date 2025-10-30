package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.storePhone;

import com.capgemini.dllpoc.api.delhaize.model.StorePhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStorePhoneRepository extends JpaRepository<StorePhone, Long> {
}

