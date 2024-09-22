package com.ing.broker.domain.model;

import com.ing.broker.dto.OrderDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set", toBuilder = true)
@Table(name ="orders")
public class Order {

  @Id
  @GeneratedValue
  @Getter
  private UUID id;
  @CreationTimestamp
  private Timestamp createdAt;
  @UpdateTimestamp
  private Timestamp updatedAt;

  private UUID customerId;
  private String assetName;
  private Side side;
  private int size;
  private float price;
  private State state;

  public static Order createFrom(UUID customerId, OrderDto orderDto) {
    return Order.newBuilder()
        .setCustomerId(customerId)
        .setSide(orderDto.getSide())
        .setSize(orderDto.getSize())
        .setState(State.PENDING)
        .setAssetName(orderDto.getAssetName())
        .setPrice(orderDto.getPrice())
        .build();
  }
}
