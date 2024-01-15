package org.example.parser;

import org.example.CronExpression;
import org.example.SubExpression;
import org.example.SubExpressionType;
import org.example.exceptions.CronParsingException;

import java.util.Set;
import java.util.TreeSet;

import static org.example.SubExpressionType.*;
import static org.example.SubExpressionType.DAY_OF_WEEK;

public class CronExpressionParser {

  private static final String CRON_FIELDS_SEPARATOR = "\\s+";
  private static final Integer MIN_SUBEXPRESSIONS_COUNT = 5;
  private static final Integer MAX_SUBEXPRESSIONS_COUNT = 6;
  private static final Integer COMMAND_SUB_EXPRESSION_POSITION = 5;

  private static final String ASTERISK = "*";
  private static final String COMMA = ",";
  private static final String SLASH = "/";
  private static final String HYPHEN = "-";

  public static final String ERROR_MESSAGE_FOR_WRONG_SUBEXPRESSIONS = "\"Unexpected format, expected in format \"minute hour day_of_month month day_of_week command\"";

  public CronExpression parse(String cronString) throws CronParsingException {
    String[] subExpressions = cronString.split(CRON_FIELDS_SEPARATOR);
    if (subExpressions.length < MIN_SUBEXPRESSIONS_COUNT || subExpressions.length > MAX_SUBEXPRESSIONS_COUNT) {
      throw new CronParsingException(ERROR_MESSAGE_FOR_WRONG_SUBEXPRESSIONS);
    }

    return CronExpression.builder()
        .cronString(cronString)
        .minutes(parseSubExpression(MINUTES, subExpressions[MINUTES.order]))
        .hours(parseSubExpression(HOURS, subExpressions[HOURS.order]))
        .dayOfMonth(parseSubExpression(DAY_OF_MONTH, subExpressions[DAY_OF_MONTH.order]))
        .month(parseSubExpression(MONTH, subExpressions[MONTH.order]))
        .dayOfWeek(parseSubExpression(DAY_OF_WEEK, subExpressions[DAY_OF_WEEK.order]))
        .command(subExpressions[COMMAND_SUB_EXPRESSION_POSITION])
        .build();
  }

  public SubExpression parseSubExpression(SubExpressionType type, String cronSubString) throws CronParsingException {
    Set<Integer> tmp = new TreeSet<>();

    if (cronSubString.equals(ASTERISK)) {
      tmp = parseAllValues(cronSubString, type);
    } else if (cronSubString.contains(COMMA)) {
      tmp = parseMultipleValues(cronSubString, type);
    } else if (cronSubString.contains(SLASH)) {
      tmp = parseIncrementalValues(cronSubString, type);
    } else if (cronSubString.contains(HYPHEN)) {
      tmp = parseRangeValues(cronSubString, type);
    } else {
      tmp = parseSingularValue(cronSubString, type);
    }

    return SubExpression.builder().cronSubString(cronSubString).type(type).values(tmp).build();
  }

  private Set<Integer> parseAllValues(String cronString, SubExpressionType type) {
    Set<Integer> tmp = new TreeSet<>();
    for (int i = type.min; i <= type.max; i++) {
      tmp.add(i);
    }
    return tmp;
  }

  private Set<Integer> parseMultipleValues(String cronString, SubExpressionType type) throws CronParsingException {
    Set<Integer> tmp = new TreeSet<>();
    for (String i : cronString.split(COMMA)) {
      int tmpValue = Integer.parseInt(i);
      validateValue(tmpValue, type);
      tmp.add(tmpValue);
    }
    return tmp;
  }

  private Set<Integer> parseIncrementalValues(String cronString, SubExpressionType type) throws CronParsingException {
    String[] values = cronString.split(SLASH);
    Set<Integer> tmp = new TreeSet<>();
    int increment = Integer.parseInt(values[1]);
    int i;

    if (values[0].equals(ASTERISK)) {
      i = type.min;
    } else {
      i = Integer.parseInt(values[0]);
    }
    validateValue(i, type);

    for (;i <= type.max; i += increment) {
      tmp.add(i);
    }
    return tmp;
  }

  private Set<Integer> parseRangeValues(String cronString, SubExpressionType type) throws CronParsingException {
    Set<Integer> tmp = new TreeSet<>();
    String[] values = cronString.split(HYPHEN);
    int min = Integer.parseInt(values[0]);
    int max = Integer.parseInt(values[1]);
    validateValue(min, type);
    validateValue(max, type);

    if (max < min) {
      throw new CronParsingException(String.format("Unexpected expression/value %s found for %s", cronString, type));
    }

    for (int i = min; i <= max; i++) {
      tmp.add(i);
    }
    return tmp;
  }

  private Set<Integer> parseSingularValue(String cronString, SubExpressionType type) throws CronParsingException {
    Set<Integer> tmp = new TreeSet<>();
    int tmpValue = Integer.parseInt(cronString);
    validateValue(tmpValue, type);
    tmp.add(tmpValue);
    return tmp;
  }

  private void validateValue(int value, SubExpressionType type) throws CronParsingException {
    if (value < type.min || value > type.max) {
      throw new CronParsingException(String.format("Unexpected expression/value %d found for %s", value, type));
    }
  }
}

