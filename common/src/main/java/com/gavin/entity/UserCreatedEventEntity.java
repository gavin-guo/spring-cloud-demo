package com.gavin.entity;

import com.gavin.enums.MessageableEventStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_CREATED_EVENT")
@NamedQuery(name = "UserCreatedEventEntity.findAll", query = "SELECT u FROM UserCreatedEventEntity u")
@DynamicInsert
@DynamicUpdate
@Data
public class UserCreatedEventEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORIGIN_ID")
    private String originId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "LOGIN_NAME")
    private String loginName;

    @Column(name = "NICK_NAME")
    private String nickName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

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
