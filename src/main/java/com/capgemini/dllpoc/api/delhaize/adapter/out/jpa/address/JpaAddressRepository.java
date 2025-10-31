package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.address;

import com.capgemini.dllpoc.api.delhaize.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
}

