package com.ing.broker.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NotEnoughAssetException extends KnownException {

  public NotEnoughAssetException(UUID customerId, String assetName) {
    super(String.format("Not enough %s found! CustomerId: %s", assetName, customerId));
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.FORBIDDEN;
  }
}
