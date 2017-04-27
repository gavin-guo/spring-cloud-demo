package com.gavin.entity;

import com.gavin.enums.AuthorityEnums;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "USER_AUTHORITY")
@Data
public class UserAuthorityEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY")
    private AuthorityEnums authority;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private UserEntity userEntity;

}
