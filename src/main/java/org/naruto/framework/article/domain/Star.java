package org.naruto.framework.article.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.naruto.framework.article.listener.StarListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="stars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class,StarListener.class})
@ToString
public class Star implements  java.io.Serializable{
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    private String userId;

    private String articleId;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

//    @DomainEvents
//    Collection<Object> domainEvents() {
//        List<Object> events= new ArrayList<Object>();
//        events.add(new StarSavedEvent(this.id,this));
//        return events;
//    }
//
//    @AfterDomainEventPublication
//    void callbackMethod() {
//        //
//    }

}
