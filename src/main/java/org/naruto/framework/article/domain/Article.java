package org.naruto.framework.article.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.naruto.framework.user.domain.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Entity
@Table(name="articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Article {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length=500)
    @NotBlank(message = "The title cannot be blank ")

    private String title;

    @Column(length=500)
    private String cover;

    @NotBlank(message = "The content cannot be blank ")
    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "longtext")
    private String contentHtml;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private int commentCount;

    private int viewCount;

    private int starCount;

    private int likeCount;

    private boolean deleted;

    private boolean recommend;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "owner")
    @Lazy(false)
    private User owner;

    private Date lastCommentAt;

    private String catalogId;

    @Version
    private Long version;
}
