package com.luka5w.gpdiscordbot.util.messages;

import java.awt.Color;

public enum EmbedType {
  INFO(Color.BLUE),
  SUCCESS(Color.GREEN),
  QUESTION(Color.BLUE),
  FAILURE(Color.RED);
  public final Color color;

  EmbedType(Color color) {
    this.color = color;
  }
}
