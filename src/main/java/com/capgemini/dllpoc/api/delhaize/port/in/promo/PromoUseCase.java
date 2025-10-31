package com.capgemini.dllpoc.api.delhaize.port.in.promo;

import com.capgemini.dllpoc.api.delhaize.model.Promo;

import java.util.List;
import java.util.Optional;

public interface PromoUseCase {
    Optional<Promo> findById(Long id);
    List<Promo> findAll();
    Promo save(Promo promo);
    void deleteById(Long id);
}

