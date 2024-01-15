package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CronExpression {
  private String cronString;
  private SubExpression minutes;
  private SubExpression hours;
  private SubExpression dayOfMonth;
  private SubExpression month;
  private SubExpression dayOfWeek;
  private String command;
}
