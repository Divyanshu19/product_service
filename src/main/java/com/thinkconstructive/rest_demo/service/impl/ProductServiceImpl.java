package com.thinkconstructive.rest_demo.service.impl;

import com.thinkconstructive.rest_demo.mapper.ProductMapper;
import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.exception.ProductNotFoundException;
import com.thinkconstructive.rest_demo.model.Product;
import com.thinkconstructive.rest_demo.repository.ProductRepository;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import com.thinkconstructive.rest_demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.hibernate.Session;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final EntityManager entityManager;

  @Override
  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = toDomainRequest(productRequest);
    productRepository.save(product);
    return productMapper.getMappedProduct(product);
  }

  @Override
  public ProductResponse updateProduct(String productId, ProductRequest productRequest) {

    return productRepository
        .findById(productId)
        .map(
            product -> {
              Product updatedProduct =
                  productMapper.updateProductFromProductRequest(product, productRequest);
              productRepository.save(updatedProduct);
              return productMapper.getMappedProduct(updatedProduct);
            })
        .orElseThrow(
            () -> {
              log.error(String.format("Product with id %s not found", productId));
              return new ProductNotFoundException("Requested Product not found, cannot be deleted");
            });
  }

  @Override
  // implement soft delete
  public String deleteProduct(String productId) {
    return productRepository
        .findById(productId)
        .map(
            product -> {
              productRepository.deleteById(productId);
              return "Product with id " + productId + " deleted successfully";
            })
        .orElseThrow(
            () -> {
              log.error(String.format("Product with id %s not found", productId));
              return new ProductNotFoundException("Requested Product not found, cannot be deleted");
            });
  }

  @Override
  public Optional<ProductResponse> getProduct(String productId) {

    return Optional.ofNullable(
        productRepository
            .findById(productId)
            .map(productMapper::getMappedProduct)
            .orElseThrow(
                () -> {
                  log.error(String.format("Product with id %s not found", productId));
                  return new ProductNotFoundException(
                      String.format("Product with id %s not found", productId));
                }));
  }

  @Override
  public Page<ProductResponse> getAllProducts(Pageable pageable, boolean showDeleted) {

    Session session = entityManager.unwrap(Session.class);

    Filter filter = session.enableFilter("deletedProductFilter");
    filter.setParameter("isDeleted", showDeleted);

    Page<ProductResponse> allProducts =
        productRepository.findAll(pageable).map(productMapper::getMappedProduct);

    session.disableFilter("deletedProductFilter");

    return allProducts;
  }

  private Product toDomainRequest(ProductRequest productRequest) {
    Product product = new Product();
    product.setProductName(productRequest.getProductName());
    product.setSellerId(productRequest.getSellerId());
    product.setQuantity(productRequest.getQuantity());
    product.setProductId(UUID.randomUUID().toString());
    product.setHiddenPrice(productRequest.getHiddenPrice());
    return product;
  }
}
