package com.schambeck.webflux.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Violation {

  private final String field;
  private final String message;
  private String value;

}
