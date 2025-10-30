package com.capgemini.dllpoc.api.delhaize.adapter.out.jpa.article;

import com.capgemini.dllpoc.api.delhaize.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaArticleRepository extends JpaRepository<Article, Long> {
}

