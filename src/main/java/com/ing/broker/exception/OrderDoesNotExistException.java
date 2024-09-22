package com.ing.broker.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrderDoesNotExistException extends KnownException {

  public OrderDoesNotExistException(UUID customerId, UUID orderId) {
    super(String.format("Order does not exist with OrderId: %s, CustomerId: %s", orderId, customerId));
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
