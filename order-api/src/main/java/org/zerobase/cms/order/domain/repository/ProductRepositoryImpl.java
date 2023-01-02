package org.zerobase.cms.order.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.zerobase.cms.order.domain.model.Product;
import org.zerobase.cms.order.domain.model.QProduct;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Product> searchByName(String name) {
		String search = "%" + name + "%";
		QProduct qProduct = QProduct.product;
		return jpaQueryFactory.selectFrom(qProduct)
			.where(qProduct.name.like(search))
			.fetch();
	}

}
