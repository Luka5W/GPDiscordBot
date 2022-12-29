package com.luka5w.gpdiscordbot.util;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import org.javacord.api.interaction.DiscordLocale;

public class TimeUtils {

  public static Duration getVMUpTime() {
    return Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
  }

  public static Instant getVMStartTime() {
    return Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime());
  }

  public static String getString(Instant instant, DiscordLocale locale) {
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
        .withLocale(getLocale(locale))
        .withZone(ZoneOffset.UTC);
    return formatter.format(instant);
  }

  private static Locale getLocale(DiscordLocale locale) {
    return Locale.forLanguageTag(locale.getLocaleCode());
  }

  public static String durationToString(Duration d) {
    return d.toString()
        .substring(2)
        .replaceAll("(\\d[HMS])(?!$)", "$1 ")
        .toLowerCase();
  }
}
