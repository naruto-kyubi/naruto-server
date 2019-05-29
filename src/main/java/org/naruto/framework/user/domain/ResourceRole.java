package org.naruto.framework.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Table(name="v_resource_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRole {

    @Id
    @Column(length = 200)
    private String resourceUrl;

    @Column(length = 400)
    private String permission;
}