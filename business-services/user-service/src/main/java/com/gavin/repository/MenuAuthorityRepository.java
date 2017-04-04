package com.gavin.repository;

import com.gavin.entity.MenuAuthorityEntity;
import com.gavin.enums.AuthorityEnums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuAuthorityRepository extends JpaRepository<MenuAuthorityEntity, String> {

    List<MenuAuthorityEntity> findByMenuId(String _menuId);

    List<MenuAuthorityEntity> findByAuthorityIn(List<AuthorityEnums> _authorities);

}
