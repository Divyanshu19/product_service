package com.thinkconstructive.rest_demo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ProductRequest {
  @NotBlank(message="{validation.missing.product.productName}")
  private final String productName;
  @NotBlank(message = "{validation.missing.product.sellerId}")
  private final String sellerId;
  @Min(value=1,message ="{validation.missing.properties.quantity}")
  private final int quantity;
  @Min(value=1,message = "{validation.missing.product.price}")
  private final int hiddenPrice;
}
