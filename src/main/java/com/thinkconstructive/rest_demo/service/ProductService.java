package com.thinkconstructive.rest_demo.service;

import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
  public void createProduct(ProductRequest product);

  public void updateProduct(String productId,ProductRequest product);

  public void deleteProduct(String productId);

  public Optional<ProductResponse> getProduct(String productId);

  public Page<ProductResponse> getAllProducts(Pageable pageable);
}
