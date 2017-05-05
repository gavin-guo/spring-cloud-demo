package com.gavin.entity;

import com.gavin.enums.PointActionEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "POINT_HISTORY")
@DynamicInsert
@DynamicUpdate
@Data
public class PointHistoryEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION")
    private PointActionEnums action;

    //@Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

}
