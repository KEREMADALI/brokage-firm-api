package com.ing.broker.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrderAlreadyProcessedException extends KnownException {

  public OrderAlreadyProcessedException(UUID customerId, UUID orderId) {
    super(String.format("Order already processed, cannot be deleted! CustomerId: %s, OrderId:%s", customerId, orderId));
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.FORBIDDEN;
  }
}
