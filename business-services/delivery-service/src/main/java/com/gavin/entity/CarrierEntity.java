package com.gavin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CARRIER", schema = "SCHEMA_DELIVERY")
@NamedQuery(name = "CarrierEntity.findAll", query = "SELECT c FROM CarrierEntity c")
@Data
public class CarrierEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;
}
