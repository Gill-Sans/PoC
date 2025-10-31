package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.article;

import com.capgemini.dllpoc.api.delhaize.model.Article;
import com.capgemini.dllpoc.api.delhaize.port.out.article.ArticleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryAdapter implements ArticleRepositoryPort {

    private final JpaArticleRepository jpaRepository;

    @Override
    public Optional<Article> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    @Transactional
    public Article save(Article article) {
        return jpaRepository.save(article);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

