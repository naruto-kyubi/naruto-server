package org.naruto.framework.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class User {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length = 20,unique = true)
    private String nickname;

    @Column(length = 20,unique = true)
    private String mobile;


    @Column(length = 50)
    private String password;

    @JsonIgnore
    @Column(length = 50)
    private String passwordSalt;

    @Column(length = 50,unique = true)
    private String mail;

    @Column(length = 50,unique = true)
    private String weibo;

    @Transient
    private String captcha;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name= "user_roles", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false) },inverseJoinColumns = { @JoinColumn(name = "role_id", nullable =false, updatable = false) })
    private Set<Role> roles= new HashSet<>();

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

}
