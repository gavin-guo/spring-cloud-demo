package com.gavin.repository;

import com.gavin.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String> {

    List<MenuEntity> findByIdIn(List<String> _ids);

}
