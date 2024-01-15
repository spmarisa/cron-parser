package org.example.exceptions;

public class CronParsingException extends Throwable {
  public CronParsingException(String message) {
    super(message);
  }
}
