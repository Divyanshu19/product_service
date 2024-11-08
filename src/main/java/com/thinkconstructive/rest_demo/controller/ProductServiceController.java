package com.thinkconstructive.rest_demo.controller;

import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import com.thinkconstructive.rest_demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    log.info("Request received for get product for id {}", productId);
    Optional<ProductResponse> productResponse = productService.getProduct(productId);
    return productResponse
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Read all products from DB
  // implement sorting , spring pagination
  @GetMapping("") // Implement pagination
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Page<ProductResponse>> getAllProductDetails(
          @PageableDefault(page = 0, size = 200,sort = "productName", direction = Sort.Direction.DESC) Pageable pageable,@RequestParam(name = "showDeleted", defaultValue = "false") boolean showDeleted) {
    log.info("Request received for getAllProducts");
    Page<ProductResponse> allProducts = productService.getAllProducts(pageable,showDeleted);
    return ResponseEntity.ok(allProducts);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED) // show the cutomer the product he created
  public ProductResponse createProductDetails(@Valid @RequestBody ProductRequest productRequest) {
    log.info("Received createProduct Request");
   return productService.createProduct(productRequest);
  }

  @PutMapping("/{productId}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse updateProductDetails(
      @PathVariable("productId") String productId,
      @Valid @RequestBody ProductRequest productRequest) {
    log.info("Received updateProduct Request");
    return productService.updateProduct(productId, productRequest);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.OK)
  public String  deleteProductDetails(@PathVariable("productId") String productId) {
    log.info("Received delete product request for id {}", productId);
   return productService.deleteProduct(productId);
  }

}
