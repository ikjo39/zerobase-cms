package org.zerobase.cms.order.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import org.zerobase.cms.order.domain.product.AddProductForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long sellerId;
	private String name;
	private String description;
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "product_id")
	private List<ProductItem> productItems = new ArrayList<>();

	public static Product of(Long sellerId, AddProductForm form) {
		return Product.builder()
			.sellerId(sellerId)
			.name(form.getName())
			.description(form.getDescription())
			.productItems(
				form.getItems().stream().map(piForm -> ProductItem.of(sellerId, piForm)).collect(Collectors.toList()))
			.build();
	}
}
