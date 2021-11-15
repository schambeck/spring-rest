package com.jobsity.rest.base.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse {

  private final List<Violation> violations = new ArrayList<>();

}
