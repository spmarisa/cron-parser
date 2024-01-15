package org.example;

public enum SubExpressionType {
  MINUTES(0, 59, 0),
  HOURS(0, 59, 1),
  DAY_OF_MONTH(1, 31, 2),
  MONTH(1, 12, 3),
  DAY_OF_WEEK(1, 5, 4);


  public final int min;
  public final int max;
  public final int order;

  private SubExpressionType(int min, int max, int order) {
    this.min = min;
    this.max = max;
    this.order = order;
  }
}
