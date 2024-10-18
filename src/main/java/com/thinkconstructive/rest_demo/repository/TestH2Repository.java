package com.thinkconstructive.rest_demo.repository;

import com.thinkconstructive.rest_demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Product,String> {}
