package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "COUNTRY", schema = "SCHEMA_ADDRESS")
@NamedQuery(name = "CountryEntity.findAll", query = "SELECT c FROM CountryEntity c")
@Data
public class CountryEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

}
