package com.capgemini.dllpoc.api.delhaize.application;

import com.capgemini.dllpoc.api.delhaize.application.promo.PromoService;
import com.capgemini.dllpoc.api.delhaize.model.Promo;
import com.capgemini.dllpoc.api.delhaize.port.out.promo.PromoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromoServiceTest {

    @Mock
    private PromoRepositoryPort repository;

    private PromoService service;

    @BeforeEach
    void setUp() {
        service = new PromoService(repository);
    }

    @Test
    void findById_returnsPromo() {
        Promo p = new Promo();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Promo> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAll_returnsList() {
        Promo p = new Promo();
        p.setId(1L);
        List<Promo> list = List.of(p);
        when(repository.findAll()).thenReturn(list);

        List<Promo> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void save_savesAndReturns() {
        Promo p = new Promo();
        p.setId(2L);
        p.setPrice(BigDecimal.valueOf(9.99));
        when(repository.save(ArgumentMatchers.any(Promo.class))).thenReturn(p);

        Promo saved = service.save(p);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());
    }

    @Test
    void deleteById_delegates() {
        doNothing().when(repository).deleteById(3L);
        service.deleteById(3L);
        verify(repository, times(1)).deleteById(3L);
    }
}
