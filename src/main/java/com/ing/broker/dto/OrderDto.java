package com.ing.broker.dto;

import com.ing.broker.domain.model.Side;
import lombok.Data;

@Data
public class OrderDto {
  private String assetName;
  private Side side;
  private int size;
  private float price;

  public float getTotalPrice(){
    return price * size;
  }
}
