package com.thinkconstructive.rest_demo.service.impl;

import com.thinkconstructive.rest_demo.mapper.ProductMapper;
import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.exception.ProductNotFoundException;
import com.thinkconstructive.rest_demo.model.Product;
import com.thinkconstructive.rest_demo.repository.ProductRepository;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import com.thinkconstructive.rest_demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  private ProductRepository productRepository;
  private ProductMapper productMapper;

  @Autowired
  public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository) {
    this.productMapper = productMapper;
    this.productRepository = productRepository;
  }

  @Override
  public void createProduct(ProductRequest productRequest) {
    Product product = toDomainRequest(productRequest);
    productRepository.save(product);
  }

  @Override
  public void updateProduct(String productId, ProductRequest productRequest) {

    productRepository
        .findById(productId)
        .ifPresentOrElse(
            product -> {
              Product updatedProduct =
                  productMapper.updateProductFromProductRequest(product, productRequest);
              productRepository.save(updatedProduct);
            },
            () -> {
              log.error(String.format("Product with id %s not found", productId));
              throw new ProductNotFoundException(
                  String.format("Product with id %s not found", productId));
            });
  }

  @Override
  // implement soft delete
  public void deleteProduct(String productId) {

    productRepository
        .findById(productId)
        .ifPresentOrElse(
            product ->
                productRepository.deleteById(
                    productId), // Perform the delete action if the product exists
            () -> {
              log.error(String.format("Product with id %s not found", productId));
              throw new ProductNotFoundException("Requested Product not found, cannot be deleted");
            } // Throw an exception if not found
            );
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
  public Page<ProductResponse> getAllProducts(Pageable pageable) {

    return productRepository
        .findAll(pageable)
        .map(product -> productMapper.getMappedProduct(product));
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
