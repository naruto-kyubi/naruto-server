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
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Comment {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @NotBlank(message = "The content cannot be blank ")
    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "longtext")
    private String contentHtml;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private int replayCount;

    private int likeCount;

    private boolean blocked;

    private boolean deleted;

    private boolean recommend;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Lazy(false)
    private User userId;

    private String articleId;


}
