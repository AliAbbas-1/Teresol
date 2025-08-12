package com.name.library.exceptions;

public class LendingNotFoundException extends RuntimeException {

  public LendingNotFoundException(String message) {
    super(message);
  }
}
