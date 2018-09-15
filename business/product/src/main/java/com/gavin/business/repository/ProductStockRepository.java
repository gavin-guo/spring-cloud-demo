package com.gavin.business.repository;

import com.gavin.business.domain.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, String> {

    ProductStock findByProductId(String _productId);

    List<ProductStock> findAllByProductIdIn(Iterable<String> _productIds);

}
