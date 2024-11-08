package com.thinkconstructive.rest_demo;

import com.thinkconstructive.rest_demo.model.Product;
import com.thinkconstructive.rest_demo.repository.ProductRepository;
import com.thinkconstructive.rest_demo.request.ProductRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProductServiceIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ProductRepository productRepository;

  @Autowired private ObjectMapper objectMapper;

  @Container static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:latest");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
  }

  @AfterEach
  public void afterEach() {
    productRepository.deleteAll();
  }

  @Test
  public void testAddProduct() throws Exception {
    ProductRequest productRequest =
        new ProductRequest("Headset", "seller:24988ssnfajkn48", 500, 1100);
    mockMvc
        .perform(
            post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
        .andExpect(status().isCreated())
        .andDo(print());
    assertEquals(1, productRepository.findAll().size());
  }

  @Test
  public void getAllProducts() throws Exception {
    List<Product> productList = new ArrayList<>();
    Product product =
        new Product(
            "P1_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    Product product2 =
        new Product(
            "P2_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    productList.add(product);
    productList.add(product2);
    productRepository.saveAll(productList);

    mockMvc
        .perform(get("/products"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.content.length()", is(productList.size())))
        .andExpect(jsonPath("$.content[1].quantity", is(product2.getQuantity())));
  }

  @Test
  public void getProduct() throws Exception {
    List<Product> productList = new ArrayList<>();
    Product product =
        new Product(
            "P1_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    Product product2 =
        new Product(
            "P2_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    productList.add(product);
    productList.add(product2);
    productRepository.saveAll(productList);

    mockMvc
        .perform(get("/products/{productId}", product2.getProductId()))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.productName", is(product2.getProductName())));
  }

  @Test
  public void updateProduct() throws Exception {

    Product product =
        new Product(
            "P1_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    productRepository.save(product);

    ProductRequest updatedProductRequest =
        new ProductRequest("Headset", "seller:24988ssnfajkn48", 50, 1100);

    mockMvc
        .perform(
            put("/products/{productId}", product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductRequest)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.productName", is(product.getProductName())));
  }

  @Test
  public void deleteProduct() throws Exception {

    Product product =
        new Product(
            "P1_" + UUID.randomUUID().toString(),
            "Headset",
            "seller:24988ssnfajkn48",
            500,
            1100,
            Boolean.FALSE);
    productRepository.save(product);

    mockMvc
        .perform(delete("/products/{productId}", product.getProductId()))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
