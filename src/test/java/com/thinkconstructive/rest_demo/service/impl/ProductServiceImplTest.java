package com.thinkconstructive.rest_demo.service.impl;

import com.thinkconstructive.rest_demo.exception.ProductNotFoundException;
import com.thinkconstructive.rest_demo.mapper.ProductMapper;
import com.thinkconstructive.rest_demo.model.Product;
import com.thinkconstructive.rest_demo.repository.ProductRepository;
import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

  @Mock private ProductRepository productRepository;
  @Mock private ProductMapper productMapper;

  @InjectMocks private ProductServiceImpl productService;

  @Test
  void shouldGetProduct() {
    Product product = new Product("P1", "Apple", "seller:24988ssnfajkn48", 5, 50, Boolean.FALSE);
    ProductResponse productResponse =
        new ProductResponse("P1", "Apple", "seller:24988ssnfajkn48", 5);
    when(productRepository.findById("P1")).thenReturn(Optional.of(product));
    when(productMapper.getMappedProduct(product)).thenReturn(productResponse);
    assertEquals(
        product.getProductName(),
        productService.getProduct(product.getProductId()).get().getProductName());
  }

  @Test
  void shouldGetAllProducts() {
    Product product = new Product("P1", "Apple", "seller:24988ssnfajkn48", 5, 52, Boolean.FALSE);
    Product product1 = new Product("P2", "Mango", "seller:24988ssnfajkn48", 5, 53, Boolean.FALSE);
    List<Product> productList = List.of(product, product1);
    Pageable pageable = PageRequest.of(0, 1);

    Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());
    when(productRepository.findAll(pageable)).thenReturn(productPage);

    assertEquals(1, productService.getAllProducts(pageable).getSize());
  }

  @Test
  void shouldCreateProduct() {
    ProductRequest productRequest = new ProductRequest("Apple", "seller:24988ssnfajkn48", 5, 52);
    productService.createProduct(productRequest);
    //  Mockito.verify(productRepository).save(any(Product.class));
    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(productRepository).save(productCaptor.capture());
    Product savedProduct = productCaptor.getValue();
    assertEquals("Apple", savedProduct.getProductName());
    assertEquals("seller:24988ssnfajkn48", savedProduct.getSellerId());
    assertEquals(5, savedProduct.getQuantity());
  }

  @Test
  void shouldDeleteProduct() {
    Product product = new Product("P1", "Apple", "seller:24988ssnfajkn48", 5, 50, Boolean.FALSE);
    when(productRepository.findById("P1")).thenReturn(Optional.of(product));
    productService.deleteProduct(product.getProductId());
    verify(productRepository).deleteById("P1");
  }

  @Test
  void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
    String productId = "P1";
    when(productRepository.findById(productId)).thenReturn(Optional.empty());
    assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
    verify(productRepository, never()).deleteById(productId);
  }

  @Test
  void shouldUpdateProduct() {
    Product existingProduct =
        new Product("P2", "Apple", "seller:24988ssnfajkn48", 5, 50, Boolean.FALSE);
    ProductRequest productRequest = new ProductRequest("New Apple", "seller:12345", 10, 60);
    when(productRepository.findById("P2")).thenReturn(Optional.of(existingProduct));
    when(productMapper.updateProductFromProductRequest(existingProduct, productRequest))
        .thenReturn(existingProduct);
    productService.updateProduct(existingProduct.getProductId(), productRequest);
    verify(productMapper).updateProductFromProductRequest(existingProduct, productRequest);
    verify(productRepository).save(existingProduct);
  }
}
