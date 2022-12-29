package com.luka5w.gpdiscordbot.util.commands;

public class InvalidArgumentException extends Throwable {

  public final int minLength;
  public final int maxLength;
  public final String[] chars;

  public InvalidArgumentException(int minLength, int maxLength, String... chars) {
    super("Illegal Argument. Expected " + minLength + "-" + maxLength + " characters of "
        + String.join(", ", chars));
    this.minLength = minLength;
    this.maxLength = maxLength;
    this.chars = chars;
  }
}
