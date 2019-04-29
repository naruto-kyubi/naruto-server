package org.naruto.framework.user.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;

    @Column(length = 20,unique = true)
    private String nickname;

    @Column(length = 20,unique = true)
    private String mobile;

    @Column(length = 50)
    private String password;

    @Column(length = 50,unique = true)
    private String mail;

    public User(String nickname, String mobile, String password, String mail) {
        this.nickname = nickname;
        this.mobile = mobile;
        this.password = password;
        this.mail = mail;
    };

    public User(){
        super();
    }

}
