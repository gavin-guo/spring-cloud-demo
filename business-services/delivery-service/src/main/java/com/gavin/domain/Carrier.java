package com.gavin.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "carrier")
@Data
public class Carrier {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

}
