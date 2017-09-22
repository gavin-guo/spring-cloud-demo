package com.gavin.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "consignee")
    private String consignee;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district;

    @Column(name = "street")
    private String street;

    @Column(name = "building")
    private String building;

    @Column(name = "room")
    private String room;

    @Column(name = "default_flag")
    private boolean defaultFlag;

    @Column(name = "comment")
    private String comment;

}
