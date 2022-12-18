package com.example.fileupload.article.domain;


import com.example.fileupload.article_image.domain.ArticleImage;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String body;

    private LocalDateTime createDate;

    private LocalDateTime updatedDate;

    //게시물에 들어있는 사진 배열들을 가져오기 위한 List
    @OneToMany(mappedBy = "article")
    private List<ArticleImage> imageList;

}
