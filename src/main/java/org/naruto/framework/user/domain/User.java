package org.naruto.framework.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;

    @Column(length = 20,unique = true)
    private String nickname;

    @Column(length = 20,unique = true)
    @NotBlank(message = "请输入手机号")
    private String mobile;

    @Column(length = 50)
    @NotBlank(message = "请输入密码")
    private String password;

    @Column(length = 50,unique = true)
    private String mail;

    @Transient
    private String captcha;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;


}
