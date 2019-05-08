package org.naruto.framework.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@Table(name="captchas")
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;

    @Column(length = 20)
    @NotBlank(message = "请输入手机号")
    private String mobile;

    @Column(length = 20)
    @NotBlank(message = "请输入验证码")
    private String captcha;

    @Column(name="create_at",columnDefinition="datetime")
    private Date createAt;


}
