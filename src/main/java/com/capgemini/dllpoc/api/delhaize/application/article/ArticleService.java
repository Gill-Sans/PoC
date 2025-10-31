package com.capgemini.dllpoc.api.delhaize.application.article;

import com.capgemini.dllpoc.api.delhaize.model.Article;
import com.capgemini.dllpoc.api.delhaize.port.in.article.ArticleUseCase;
import com.capgemini.dllpoc.api.delhaize.port.out.article.ArticleRepositoryPort;
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
public class ArticleService implements ArticleUseCase {

    private final ArticleRepositoryPort repository;

    @Override
    public Optional<Article> findById(@NotNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Article save(@NotNull @Valid Article article) {
        return repository.save(article);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }
}
