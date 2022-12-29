package com.luka5w.gpdiscordbot.util;

import org.javacord.api.interaction.DiscordLocale;

public class MissingTranslationException extends RuntimeException {

  public MissingTranslationException(String key) {
    super("Missing all translation for key '" + key + "'");
  }

  public MissingTranslationException(String key, DiscordLocale locale) {
    super("Missing " + locale.getLocaleCode() + " translation for key '" + key + "'");
  }
}
