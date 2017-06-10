package com.gavin.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
@Data
public class CountryEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

}
