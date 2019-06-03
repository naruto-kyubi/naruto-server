package org.naruto.framework.user.domain;

import lombok.Data;

import javax.persistence.*;

@Embeddable
@Access(AccessType.FIELD)
@Data
public class Geographic{
    @AttributeOverrides({@AttributeOverride(name="label", column=@Column(name = "province_label", length = 255)),
            @AttributeOverride(name="key", column = @Column(name = "province_key", length = 255))})
    private KeyLabel province;

    @AttributeOverrides({@AttributeOverride(name="label", column=@Column(name = "city_label", length = 255)),
            @AttributeOverride(name="key", column = @Column(name = "city_key", length = 255))})
    private KeyLabel city;

}
