package org.zerobase.cms.order.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.cms.order.domain.model.Product;

// 하위 엔티티 가져올때 기본 세팅은 lazy - product -> productItem은 필요할 때마다 가져옴
// 이게 n + 1 문제를 야기시킴, productItem 하나에 할당된 변수 전부를 하나하나 쿼리를 날려 가져옴
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long id);

	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
	Optional<Product> findBySellerIdAndId(Long sellerId, Long productId);

	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
	List<Product> findAllByIdIn(List<Long> ids);
}
