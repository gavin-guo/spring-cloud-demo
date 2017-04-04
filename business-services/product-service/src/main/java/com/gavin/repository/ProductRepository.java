package com.gavin.repository;

import com.gavin.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    Page<ProductEntity> findByCategoryId(String _categoryId, Pageable _pageable);

}
