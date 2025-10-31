package com.capgemini.dllpoc.api.delhaize.application;

import com.capgemini.dllpoc.api.delhaize.application.address.AddressService;
import com.capgemini.dllpoc.api.delhaize.model.Address;
import com.capgemini.dllpoc.api.delhaize.port.out.address.AddressRepositoryPort;
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
public class AddressServiceTest {

    @Mock
    private AddressRepositoryPort repository;

    private AddressService service;

    @BeforeEach
    void setUp() {
        service = new AddressService(repository);
    }

    @Test
    void findById_returnsAddress() {
        Address a = new Address();
        a.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        Optional<Address> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAll_returnsList() {
        Address a = new Address();
        a.setId(1L);
        List<Address> list = List.of(a);
        when(repository.findAll()).thenReturn(list);

        List<Address> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void save_savesAndReturns() {
        Address a = new Address();
        a.setId(2L);
        when(repository.save(ArgumentMatchers.any(Address.class))).thenReturn(a);

        Address saved = service.save(a);
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
