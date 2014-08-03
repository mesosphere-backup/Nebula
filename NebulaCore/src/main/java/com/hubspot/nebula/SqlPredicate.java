package com.hubspot.nebula;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlPredicate {
  public String columnName() default "";
  public Operator operator() default Operator.EQUALS;

  public static enum Operator {
    EQUALS("="),
    NOT_EQUAL("!="),
    LESS_THAN("<"),
    GREATER_THAN(">");

    private String value;

    private Operator(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}