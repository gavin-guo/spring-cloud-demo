package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PROVINCE")
@Data
public class ProvinceEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    private CountryEntity countryEntity;

}
