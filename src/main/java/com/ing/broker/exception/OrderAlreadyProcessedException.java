package com.ing.broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class OrderAlreadyProcessedException extends RuntimeException {

  public OrderAlreadyProcessedException(UUID customerId, UUID orderId) {
    super(String.format("Order already processed, cannot be deleted! CustomerId: %s, OrderId:%s", customerId, orderId));
  }
}
