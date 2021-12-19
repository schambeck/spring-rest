package com.schambeck.webflux.exception;

import lombok.Data;

@Data
public class Violation {

  private final String field;
  private final String message;
  private final String value;

}
