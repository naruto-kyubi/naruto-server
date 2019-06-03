package org.naruto.framework.user.domain;


import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@Data
public class KeyLabel {
    private String label;
    private String key;
}