package com.gavin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
@Data
public class AddressEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "DISTRICT_ID", referencedColumnName = "ID")
    private DistrictEntity districtEntity;

    @Column(name = "STREET")
    private String street;

    @Column(name = "BUILDING")
    private String building;

    @Column(name = "ROOM")
    private String room;

    @Column(name = "DEFAULT_FLAG")
    private Boolean defaultFlag;

    @Column(name = "COMMENT")
    private String comment;

}
