package com.gavin.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "district")
@Data
public class District {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

}
