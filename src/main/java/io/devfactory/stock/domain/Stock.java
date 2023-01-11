package io.devfactory.stock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_stock")
@Entity
public class Stock {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Long productId;

  private Long quantity;

  // OptimisticLock를 위해 선언
  @Version
  private Long version;

  public Stock(Long productId, Long quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public void decrease(Long quantity) {
    if (this.quantity - quantity < 0) {
      throw new IllegalStateException("Quantity cannot be less than 0...");
    }

    this.quantity -= quantity;
  }

}
