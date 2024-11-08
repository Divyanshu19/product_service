package com.thinkconstructive.rest_demo.repository;

import com.thinkconstructive.rest_demo.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;



  @Test
  void saveProduct() {
    String productId = "P1_" + UUID.randomUUID().toString();
    Product product =
        new Product(productId, "Headphone", "seller:24988ssnfajkn48", 50, 1100, Boolean.FALSE);
    productRepository.save(product);
    Assertions.assertEquals(1, productRepository.findAll().size());
    Assertions.assertEquals(
        product.getProductId(), productRepository.findById(productId).orElseThrow().getProductId());
    Assertions.assertEquals(
        product.getProductName(),
        productRepository.findById(productId).orElseThrow().getProductName());
  }

  @Test
  void testGetProductById() {
    String productId = "P1_" + UUID.randomUUID().toString();
    Product product =
        new Product(productId, "Headset", "seller:24988ssnfajkn48", 500, 1100, Boolean.FALSE);

    productRepository.save(product);

    Optional<Product> retrievedProduct = productRepository.findById(productId);

    Assertions.assertEquals(product.getProductId(), retrievedProduct.get().getProductId());
    Assertions.assertEquals(product.getProductName(), retrievedProduct.get().getProductName());
  }

  @Test
  void testDeleteProductById() {
    String productId = "P1_" + UUID.randomUUID().toString();
    Product product =
        new Product(productId, "Headset", "seller:24988ssnfajkn48", 500, 1100, Boolean.FALSE);
    productRepository.save(product);

    Optional<Product> savedProduct = productRepository.findById(productId);
    productRepository.deleteById(productId);

    Optional<Product> deletedProduct = productRepository.findById(productId);
      Assertions.assertTrue(deletedProduct.isPresent(), "Product should still be present but marked as deleted");

      Assertions.assertEquals(Boolean.TRUE, deletedProduct.get().isDeleted());
  }

    @Test
    void testUpdateProduct() {
        String productId = "P2_" + UUID.randomUUID().toString();
        Product product = new Product(
                productId,
                "Headset",
                "seller:24988ssnfajkn48",
                500,
                1100,
                Boolean.FALSE
        );
        productRepository.save(product);

        Product savedProduct = productRepository.findById(productId).get();

        savedProduct.setProductName("Iphone");
        savedProduct.setQuantity(5);
        savedProduct.setSellerId("Apple");

        productRepository.save(savedProduct);

        Optional<Product> updatedProduct = productRepository.findById(productId);
        Assertions.assertTrue(updatedProduct.isPresent(), "Updated product should still be present in the database");
        Assertions.assertEquals("Iphone", updatedProduct.get().getProductName());
        Assertions.assertEquals(5, updatedProduct.get().getQuantity());

    }
}
