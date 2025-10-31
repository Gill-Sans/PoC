package com.capgemini.dllpoc.api.delhaize.port.in.article;

import com.capgemini.dllpoc.api.delhaize.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleUseCase {
    Optional<Article> findById(Long id);
    List<Article> findAll();
    Article save(Article article);
    void deleteById(Long id);
}

