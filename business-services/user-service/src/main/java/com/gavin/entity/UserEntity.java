package com.gavin.entity;

import com.gavin.enums.UserStatusEnums;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
@DynamicInsert
@DynamicUpdate
@Data
public class UserEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "LOGIN_NAME")
    private String loginName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NICK_NAME")
    private String nickName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatusEnums status;

    @Column(name = "GRADE")
    private Byte grade;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserAuthorityEntity> userAuthorityEntities;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "MODIFIED_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

    public UserAuthorityEntity addUserAuthorityEntity(UserAuthorityEntity userAuthority) {
        if (CollectionUtils.isEmpty(userAuthorityEntities)) {
            userAuthorityEntities = new ArrayList<>();
        }
        userAuthorityEntities.add(userAuthority);
        userAuthority.setUserEntity(this);

        return userAuthority;
    }

    public UserAuthorityEntity removeUserAuthorityEntity(UserAuthorityEntity userAuthority) {
        userAuthorityEntities.remove(userAuthority);
        userAuthority.setUserEntity(null);

        return userAuthority;
    }

}
