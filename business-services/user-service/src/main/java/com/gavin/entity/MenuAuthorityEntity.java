package com.gavin.entity;

import com.gavin.enums.AuthorityEnums;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MENU_AUTHORITY", schema = "SCHEMA_USER")
@NamedQuery(name = "MenuAuthorityEntity.findAll", query = "SELECT m FROM MenuAuthorityEntity m")
@Data
public class MenuAuthorityEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "MENU_ID")
    private String menuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY")
    private AuthorityEnums authority;

}
