package com.gavin.entity;

import com.gavin.enums.PaymentStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PAYMENT", schema = "SCHEMA_PAYMENT")
@NamedQuery(name = "PaymentEntity.findAll", query = "SELECT p FROM PaymentEntity p")
@DynamicInsert
@DynamicUpdate
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private PaymentStatusEnums status;

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
