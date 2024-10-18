package com.thinkconstructive.rest_demo.controller;

import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import com.thinkconstructive.rest_demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductServiceController {
  private final ProductService productService;

  // Read specific product from DB
  @GetMapping("/{productId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductResponse> getProductDetails(
      @PathVariable("productId") String productId) {
    Optional<ProductResponse> productResponse = productService.getProduct(productId);
    log.info("Request received for get product for id {}", productId);
    return productResponse
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Read all products from DB
  // implement sorting , spring pagination
  @GetMapping("") // Implement pagination
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Page<ProductResponse>> getAllProductDetails(
          Pageable pageable) {
    log.info("Request received for getAllProducts");
    Page<ProductResponse> allProducts = productService.getAllProducts(pageable);
    return ResponseEntity.ok(allProducts);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createProductDetails(@Valid @RequestBody ProductRequest productRequest) {
    log.info("Received createProduct Request");
    productService.createProduct(productRequest);
  }

  @PutMapping("/{productId}")
  @ResponseStatus(HttpStatus.OK)
  public void updateProductDetails(
      @PathVariable("productId") String productId,
      @Valid @RequestBody ProductRequest productRequest) {
    log.info("Received updateProduct Request");
    productService.updateProduct(productId, productRequest);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProductDetails(@PathVariable("productId") String productId) {
    log.info("Received delete product request for id {}", productId);
    productService.deleteProduct(productId);
  }
}
