package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SubExpression {
  private String cronSubString;
  private SubExpressionType type;
  private Set<Integer> values;
}
