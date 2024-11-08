package com.thinkconstructive.rest_demo.service;

import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
  public ProductResponse createProduct(ProductRequest product);

  public ProductResponse updateProduct(String productId,ProductRequest product);

  public String  deleteProduct(String productId);

  public Optional<ProductResponse> getProduct(String productId);

  public Page<ProductResponse> getAllProducts(Pageable pageable,boolean showDeleted);
}
