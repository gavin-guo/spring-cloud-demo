package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "DISTRICT")
@Data
public class DistrictEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "CITY_ID", referencedColumnName = "ID")
    private CityEntity cityEntity;

}
