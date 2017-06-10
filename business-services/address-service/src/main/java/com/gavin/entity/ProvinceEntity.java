package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "province")
@Data
public class ProvinceEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private CountryEntity countryEntity;

}
