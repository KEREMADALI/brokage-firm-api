package com.ing.broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotEnoughMoneyException extends RuntimeException {

  public NotEnoughMoneyException(UUID customerId) {
    super(String.format("Not enough TRY asset found! CustomerId: %s", customerId));
  }
}
