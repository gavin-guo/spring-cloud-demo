package com.gavin.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
@Data
public class Country {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

}
