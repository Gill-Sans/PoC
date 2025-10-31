package com.capgemini.dllpoc.api.delhaize.application;

import com.capgemini.dllpoc.api.delhaize.application.store.StoreService;
import com.capgemini.dllpoc.api.delhaize.model.Store;
import com.capgemini.dllpoc.api.delhaize.port.out.store.StoreRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class StoreServiceTest {

    @Mock
    private StoreRepositoryPort repository;

    private StoreService service;

    @BeforeEach
    void setUp() {
        service = new StoreService(repository);
    }

    @Test
    void findById_returnsStore() {
        Store s = new Store();
        s.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(s));

        Optional<Store> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findByStoreNumber_returnsStore() {
        Store s = new Store();
        s.setId(2L);
        when(repository.findByStoreNumber(100L)).thenReturn(Optional.of(s));

        Optional<Store> result = service.findByStoreNumber(100L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
    }

    @Test
    void findAll_returnsList() {
        Store s = new Store();
        s.setId(1L);
        List<Store> list = List.of(s);
        when(repository.findAll()).thenReturn(list);

        List<Store> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void save_savesAndReturns() {
        Store s = new Store();
        s.setId(2L);
        when(repository.save(ArgumentMatchers.any(Store.class))).thenReturn(s);

        Store saved = service.save(s);
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
