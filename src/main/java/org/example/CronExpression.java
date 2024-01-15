package org.example;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

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

  public String toString() {
    return String.format("%-13s%s\n", minutes.getType(), formatSetType(minutes.getValues())) +
        String.format("%-13s%s\n", hours.getType(), formatSetType(hours.getValues())) +
        String.format("%-13s%s\n", dayOfMonth.getType(), formatSetType(dayOfMonth.getValues())) +
        String.format("%-13s%s\n", month.getType(), formatSetType(month.getValues())) +
        String.format("%-13s%s\n", dayOfWeek.getType(), formatSetType(dayOfWeek.getValues())) +
        String.format("%-13s%s\n", "command", command);
  }

  private String formatSetType(Set<Integer> values) {
    return values.stream().map(Object::toString).collect(Collectors.joining(" "));
  }
}
