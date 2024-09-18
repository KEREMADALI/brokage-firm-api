package com.ing.broker.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set", toBuilder = true)
@Table(name = "assets")
public class Asset {

  @Id
  @GeneratedValue
  @Getter
  private UUID id;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  private UUID customerId;
  private String name;
  private int size;
  private int usableSize;
}
