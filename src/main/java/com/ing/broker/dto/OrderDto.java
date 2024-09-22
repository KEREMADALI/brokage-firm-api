package com.ing.broker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ing.broker.domain.model.Side;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set", toBuilder = true)
public class OrderDto {
  private String assetName;
  private Side side;
  private int size;
  private int price;

  @JsonIgnore
  public int getTotalValue(){
    return price * size;
  }
}
