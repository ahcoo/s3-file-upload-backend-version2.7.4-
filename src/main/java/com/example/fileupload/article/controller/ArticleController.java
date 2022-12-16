package com.example.fileupload.article.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.fileupload.article.domain.Article;
import com.example.fileupload.article.dto.CreateArticleForm;
import com.example.fileupload.article.service.ArticleService;
import com.example.fileupload.aws.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final AwsService awsService;
    private final ArticleService articleService;
//    private final ArticleImageService articleImageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("")
    public List<Article> getArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping("")
    public void createArticle(@Valid CreateArticleForm createArticleForm, @RequestParam(value="files", required = false) List<MultipartFile> files) throws IOException {

        String originalFilename = files.get(0).getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(files.get(0).getInputStream().available());

        amazonS3.putObject(bucket, originalFilename, files.get(0).getInputStream(), objectMetadata);
//        Article article = articleService.createArticle(createArticleForm);
//
//        String imgUrl = awsService.sendFileToS3Bucket(files.get(0));

        articleService.createArticle(createArticleForm);
    }
}
