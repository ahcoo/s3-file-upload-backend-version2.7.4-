package com.example.fileupload.article.service;

import com.example.fileupload.article.dao.ArticleRepository;
import com.example.fileupload.article.domain.Article;
import com.example.fileupload.article.dto.ArticleDto;
import com.example.fileupload.article.dto.CreateArticleForm;
import com.example.fileupload.article_image.domain.ArticleImage;
import com.example.fileupload.article_image.service.ArticleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final ArticleImageService articleImageService;

    public List<ArticleDto> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(article -> {
                    ArticleDto articleDto = new ArticleDto(article);
                    return articleDto;
                })
                .collect(Collectors.toList());
    }

    public Article createArticle(CreateArticleForm createArticleForm) {
        Article article = Article.builder()
                .title(createArticleForm.getTitle())
                .body(createArticleForm.getBody())
                .createDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        Article article1 = articleRepository.save(article);
        articleImageService.setArticleAtArticleImageList(article1, createArticleForm.getImageIdList());
        return article;
    }


    public ArticleDto getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        ArticleDto articleDto = new ArticleDto(article);
//        사람(20)
//        사람(int 나이) {
//            this.나이 = 나이;
//        }

        return articleDto;



    }
}
