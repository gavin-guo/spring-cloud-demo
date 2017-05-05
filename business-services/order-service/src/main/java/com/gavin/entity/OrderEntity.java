package com.gavin.entity;

import com.gavin.enums.OrderStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`ORDER`")
@DynamicInsert
@DynamicUpdate
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatusEnums status;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "REWARD_POINTS")
    private BigDecimal rewardPoints;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    //@Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "MODIFIED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemEntity> itemEntities;

    public ItemEntity addItemEntity(ItemEntity itemEntity) {
        if (CollectionUtils.isEmpty(itemEntities)) {
            itemEntities = new ArrayList<>();
        }
        itemEntities.add(itemEntity);
        itemEntity.setOrderEntity(this);

        return itemEntity;
    }

    public ItemEntity removeItemEntity(ItemEntity itemEntity) {
        itemEntities.remove(itemEntity);
        itemEntity.setOrderEntity(null);

        return itemEntity;
    }

}
