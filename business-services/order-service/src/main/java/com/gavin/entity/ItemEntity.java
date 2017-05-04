package com.gavin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ITEM")
@Data
public class ItemEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private OrderEntity orderEntity;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "QUANTITY")
    private Integer quantity;

}
