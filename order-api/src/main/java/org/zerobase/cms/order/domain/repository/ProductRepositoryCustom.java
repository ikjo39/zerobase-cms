package org.zerobase.cms.order.domain.repository;

import java.util.List;

import org.zerobase.cms.order.domain.model.Product;

public interface ProductRepositoryCustom {
	List<Product> searchByName(String name);
}
