package com.jobsity.rest.base.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationErrorResponse {

  private List<Violation> violations = new ArrayList<>();

  public Collection<Violation> getViolations() {
    return violations;
  }

}
