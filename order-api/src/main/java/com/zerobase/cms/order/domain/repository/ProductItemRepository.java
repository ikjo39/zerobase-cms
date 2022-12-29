package com.zerobase.cms.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.cms.order.domain.model.ProductItem;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
