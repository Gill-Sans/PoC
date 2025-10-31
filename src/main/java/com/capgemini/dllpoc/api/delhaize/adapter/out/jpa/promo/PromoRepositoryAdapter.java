package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.promo;

import com.capgemini.dllpoc.api.delhaize.model.Promo;
import com.capgemini.dllpoc.api.delhaize.port.out.promo.PromoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromoRepositoryAdapter implements PromoRepositoryPort {

    private final JpaPromoRepository jpaRepository;

    @Override
    public Optional<Promo> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Promo> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    @Transactional
    public Promo save(Promo promo) {
        return jpaRepository.save(promo);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

