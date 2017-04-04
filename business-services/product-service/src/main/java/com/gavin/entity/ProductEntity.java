package com.gavin.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRODUCT", schema = "SCHEMA_PRODUCT")
@NamedQuery(name = "ProductEntity.findAll", query = "SELECT p FROM ProductEntity p")
@DynamicInsert
@DynamicUpdate
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CATEGORY_ID")
    private String categoryId;

    @Column(name = "PRICE")
    private Float price;

    @Column(name = "STOCKS")
    private Integer stocks;

    @Column(name = "COMMENT")
    private String comment;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "MODIFIED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

}
