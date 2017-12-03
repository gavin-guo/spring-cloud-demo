package com.gavin.business.domain;

import com.gavin.common.enums.OrderStatusEnums;
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
@Table(name = "`order`")
@DynamicInsert
@DynamicUpdate
@Data
public class Order {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusEnums status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "reward_points")
    private BigDecimal rewardPoints;

    @Column(name = "consignee")
    private String consignee;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "modified_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Item> items;

    public Item addItem(Item item) {
        if (CollectionUtils.isEmpty(items)) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setOrder(this);

        return item;
    }

    public Item removeItem(Item item) {
        items.remove(item);
        item.setOrder(null);

        return item;
    }

}
