package com.thinkconstructive.rest_demo.responses;

import lombok.Data;

@Data
public class ProductResponse {
  private final String productId;
  private final String productName;
  private final String sellerId;
  private final int quantity;

}
