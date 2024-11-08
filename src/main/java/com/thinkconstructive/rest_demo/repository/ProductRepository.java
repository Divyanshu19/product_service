package com.thinkconstructive.rest_demo.repository;

import com.thinkconstructive.rest_demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {}
