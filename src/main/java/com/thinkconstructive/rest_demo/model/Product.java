package com.thinkconstructive.rest_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product_details")
@EqualsAndHashCode
@SQLDelete(sql="UPDATE product_details SET deleted = true WHERE product_id=?")
@Where(clause = "deleted=false")
public class Product {
  @Id
  private String productId;
  private String productName;
  private String sellerId;
  private int quantity;
  private int hiddenPrice;
  private boolean deleted= Boolean.FALSE;
}
