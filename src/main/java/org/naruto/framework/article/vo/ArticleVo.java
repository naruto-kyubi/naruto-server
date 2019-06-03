package org.naruto.framework.article.vo;


import lombok.Data;

import java.util.Date;

@Data
public class ArticleVo {
    private String id;
    private String owner;
    private String title;
    private String avatar;
    private Date updatedAt;
    private Date createdAt;
    private int likeCount;
    private int starCount;
    private int commentCount;
    private String content;
}
