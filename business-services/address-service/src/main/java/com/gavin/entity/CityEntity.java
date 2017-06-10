package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "city")
@Data
public class CityEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "municipality")
    private boolean municipality;

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    private ProvinceEntity provinceEntity;

}
