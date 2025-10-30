package com.capgemini.dllpoc.api.delhaize.application;

import com.capgemini.dllpoc.api.delhaize.application.article.ArticleService;
import com.capgemini.dllpoc.api.delhaize.model.Article;
import com.capgemini.dllpoc.api.delhaize.port.out.article.ArticleRepositoryPort;
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
public class ArticleServiceTest {

    @Mock
    private ArticleRepositoryPort repository;

    private ArticleService service;

    @BeforeEach
    void setUp() {
        service = new ArticleService(repository);
    }

    @Test
    void findById_returnsArticle() {
        Article a = new Article();
        a.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        Optional<Article> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAll_returnsList() {
        Article a = new Article();
        a.setId(1L);
        List<Article> list = List.of(a);
        when(repository.findAll()).thenReturn(list);

        List<Article> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void save_savesAndReturns() {
        Article a = new Article();
        a.setId(2L);
        when(repository.save(ArgumentMatchers.any(Article.class))).thenReturn(a);

        Article saved = service.save(a);
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
