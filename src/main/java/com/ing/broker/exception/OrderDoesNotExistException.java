package com.ing.broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OrderDoesNotExistException extends RuntimeException {

  public OrderDoesNotExistException(UUID customerId, UUID orderId) {
    super(String.format("Order does not exist with OrderId: %s, CustomerId: %s", orderId, customerId));
  }
}
