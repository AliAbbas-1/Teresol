package com.name.library.exceptions;

public class AlreadyReturnedException extends RuntimeException {

  public AlreadyReturnedException(String message) {
    super(message);
  }
}
