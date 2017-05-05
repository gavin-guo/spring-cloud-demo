package com.gavin.entity;

import com.gavin.enums.DeliveryStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DELIVERY")
@DynamicInsert
@DynamicUpdate
@Data
public class DeliveryEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToOne
    @JoinColumn(name = "CARRIER_ID", referencedColumnName = "ID")
    private CarrierEntity carrierEntity;

    @Column(name = "TRACKING_NUMBER")
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private DeliveryStatusEnums status;

    //@Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "MODIFIED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

}
