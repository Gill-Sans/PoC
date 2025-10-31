package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.promo;

import com.capgemini.dllpoc.api.delhaize.model.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPromoRepository extends JpaRepository<Promo, Long> {
}

