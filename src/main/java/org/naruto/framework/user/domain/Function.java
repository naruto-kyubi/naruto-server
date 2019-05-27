package org.naruto.framework.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="functions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Function {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length = 40)
    private String parentId;

    @Column(length = 500)
    private String path;

    @Column(length = 500)
    private String locale;

    @Column(length = 500)
    private String code;

    @Column(length = 500)
    private String name;

    @Column(length = 200)
    private String type;

    private Integer seq;
}
