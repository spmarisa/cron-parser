package org.example.parser;

import org.example.SubExpression;
import org.example.exceptions.CronParsingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.example.SubExpressionType.*;
import static org.example.SubExpressionType.MINUTES;
import static org.example.parser.CronExpressionParser.ERROR_MESSAGE_FOR_WRONG_SUBEXPRESSIONS;
import static org.junit.jupiter.api.Assertions.*;

class CronExpressionParserTest {

  CronExpressionParser cronExpressionParser = new CronExpressionParser();

  @Test
  public void parseSubExpression_withSingleValue() throws CronParsingException {
    SubExpression subExpression = cronExpressionParser.parseSubExpression(MINUTES, "2");
    Set<Integer> expectedOutput =  new TreeSet<>(Arrays.asList(2));

    assertEquals(expectedOutput, subExpression.getValues());
  }

  @Test
  public void parseSubExpression_withAsterix() throws CronParsingException {
    SubExpression subExpression = cronExpressionParser.parseSubExpression(MONTH, "*");
    Set<Integer> expectedOutput =  new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

    assertEquals(expectedOutput, subExpression.getValues());
  }

  @Test
  public void parseSubExpression_withComma() throws CronParsingException {
    SubExpression subExpression = cronExpressionParser.parseSubExpression(HOURS, "1,11");
    Set<Integer> expectedOutput =  new TreeSet<>(Arrays.asList(1, 11));

    assertEquals(expectedOutput, subExpression.getValues());
  }

  @Test
  public void parseSubExpression_withSlash() throws CronParsingException {
    SubExpression subExpression = cronExpressionParser.parseSubExpression(MINUTES, "*/15");
    Set<Integer> expectedOutput =  new TreeSet<>(Arrays.asList(0, 15, 30, 45));

    assertEquals(expectedOutput, subExpression.getValues());
  }

  @Test
  public void parserExpression_whenWrongNumberOfArgumentsGiven() {
    CronParsingException exception = assertThrows(CronParsingException.class, () -> cronExpressionParser.parse("* * * * * * * *"));
    assertEquals(ERROR_MESSAGE_FOR_WRONG_SUBEXPRESSIONS, exception.getMessage());
  }

  @Test
  public void parserExpression_whenWrongValueGivenForMinute() {
    CronParsingException exception = assertThrows(CronParsingException.class, () -> cronExpressionParser.parseSubExpression(MINUTES, "62"));
    assertEquals("Unexpected expression/value 62 found for MINUTES", exception.getMessage());
  }

  @Test
  public void parserExpression_whenWrongValueGivenForMonth() {
    CronParsingException exception = assertThrows(CronParsingException.class, () -> cronExpressionParser.parseSubExpression(MONTH, "14/2"));
    assertEquals("Unexpected expression/value 14 found for MONTH", exception.getMessage());
  }

  @Test
  public void parserExpression_whenWrongRangeValuesAreGiven() {
    CronParsingException exception = assertThrows(CronParsingException.class, () -> cronExpressionParser.parseSubExpression(HOURS, "6-5"));
    assertEquals("Unexpected expression/value 6-5 found for HOURS", exception.getMessage());
  }
}