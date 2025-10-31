package com.capgemini.dllpoc.api.delhaize.application.promo;

import com.capgemini.dllpoc.api.delhaize.model.Promo;
import com.capgemini.dllpoc.api.delhaize.port.in.promo.PromoUseCase;
import com.capgemini.dllpoc.api.delhaize.port.out.promo.PromoRepositoryPort;
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
public class PromoService implements PromoUseCase {

    private final PromoRepositoryPort repository;

    @Override
    public Optional<Promo> findById(@NotNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Promo> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Promo save(@NotNull @Valid Promo promo) {
        return repository.save(promo);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }
}
