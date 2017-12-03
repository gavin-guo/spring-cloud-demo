package com.gavin.business.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "province")
@Data
public class Province {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

}
