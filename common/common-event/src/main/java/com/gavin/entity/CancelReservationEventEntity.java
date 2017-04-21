package com.gavin.entity;

import com.gavin.enums.MessageableEventStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CANCEL_RESERVATION_EVENT")
@NamedQuery(name = "CancelReservationEventEntity.findAll", query = "SELECT c FROM CancelReservationEventEntity c")
@DynamicInsert
@DynamicUpdate
@Data
public class CancelReservationEventEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORIGIN_ID")
    private String originId;

    @Column(name = "ORDER_ID")
    private String orderId;

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
