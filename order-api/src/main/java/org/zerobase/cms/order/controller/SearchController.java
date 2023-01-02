package org.zerobase.cms.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.cms.order.domain.product.ProductDto;
import org.zerobase.cms.order.service.ProductSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search/product")
@RequiredArgsConstructor
public class SearchController {
	private final ProductSearchService productSearchService;

	@GetMapping
	public ResponseEntity<List<ProductDto>> searchByName(@RequestParam String name) {
		return ResponseEntity.ok(
			productSearchService.searchByName(name).stream()
				.map(ProductDto::withoutItemsfrom).collect(Collectors.toList())
		);
	}

	@GetMapping("/detail")
	public ResponseEntity<ProductDto> getDetail(@RequestParam Long productId) {
		return ResponseEntity.ok(
			ProductDto.from(productSearchService.getByProductId(productId))
		);
	}
}
