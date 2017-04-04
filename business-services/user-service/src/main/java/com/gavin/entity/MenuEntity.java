package com.gavin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MENU", schema = "SCHEMA_USER")
@NamedQuery(name = "MenuEntity.findAll", query = "SELECT m FROM MenuEntity m")
@Data
public class MenuEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "PARENT_ID")
    private String parentId;

}
