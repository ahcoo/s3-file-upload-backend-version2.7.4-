package com.example.fileupload.article.service;

import com.example.fileupload.article.dao.ArticleRepository;
import com.example.fileupload.article.domain.Article;
import com.example.fileupload.article.dto.CreateArticleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article createArticle(CreateArticleForm createArticleForm) {
        Article article = Article.builder()
                .title(createArticleForm.getTitle())
                .body(createArticleForm.getBody())
                .createDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        articleRepository.save(article);
        return article;
    }


}
