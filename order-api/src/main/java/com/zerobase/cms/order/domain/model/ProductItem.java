package com.zerobase.cms.order.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.zerobase.cms.order.domain.product.AddProductItemForm;

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
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long sellerId;
	@Audited
	private String name;
	@Audited
	private Integer price;
	private Integer count;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public static ProductItem of(Long sellerId, AddProductItemForm form) {
		return ProductItem.builder()
			.sellerId(sellerId)
			.name(form.getName())
			.price(form.getPrice())
			.count(form.getCount())
			.build();
	}
}
