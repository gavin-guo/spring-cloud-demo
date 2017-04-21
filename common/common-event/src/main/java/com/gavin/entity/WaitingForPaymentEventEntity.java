package com.gavin.entity;

import com.gavin.enums.MessageableEventStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "WAITING_FOR_PAYMENT_EVENT")
@NamedQuery(name = "WaitingForPaymentEventEntity.findAll", query = "SELECT w FROM WaitingForPaymentEventEntity w")
@DynamicInsert
@DynamicUpdate
@Data
public class WaitingForPaymentEventEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORIGIN_ID")
    private String originId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private MessageableEventStatusEnums status;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "MODIFIED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

}
