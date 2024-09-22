package com.ing.broker.exception;

import org.springframework.http.HttpStatus;

public abstract class KnownException extends RuntimeException{
  public KnownException(String message){
    super(message);
  }

  public abstract HttpStatus getStatus() ;
}
