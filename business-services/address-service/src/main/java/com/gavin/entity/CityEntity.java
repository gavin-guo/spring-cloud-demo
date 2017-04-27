package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CITY")
@Data
public class CityEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID")
    private ProvinceEntity provinceEntity;

}
