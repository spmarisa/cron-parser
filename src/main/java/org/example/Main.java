package org.example;

import org.example.parser.CronExpressionParser;

public class Main {
  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("please input only 1 argument");
    }

    CronExpressionParser cronExpressionParser = new CronExpressionParser();
    CronExpression cronExpression = cronExpressionParser.parse(args[0]);
  }
}
