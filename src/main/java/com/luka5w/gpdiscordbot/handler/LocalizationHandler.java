package com.luka5w.gpdiscordbot.handler;

import com.luka5w.gpdiscordbot.util.MissingTranslationException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.interaction.DiscordLocale;

/**
 * Holds all translations of text (usually Messages) for different locales.
 */
public class LocalizationHandler {

  private static final Logger LOGGER = LogManager.getLogger(LocalizationHandler.class);

  /**
   * The charset used to read the lang files.
   */
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  /**
   * The default (fallback locale).
   */
  public static final DiscordLocale DEFAULT = DiscordLocale.ENGLISH_US;

  /**
   * All translations
   */
  private static final Map<String, Map<DiscordLocale, String>> TRANSLATIONS = new HashMap<>();

  // load all lang files into LOCALIZATIONS
  static {
    load(DEFAULT);
    load(DiscordLocale.GERMAN);
  }

  /**
   * Loads a lang file into {@link #TRANSLATIONS}.
   *
   * @param locale The locale to load
   */
  private static void load(DiscordLocale locale) {
    InputStream stream = Objects.requireNonNull(
        LocalizationHandler.class.getClassLoader().getResourceAsStream(locale.getLocaleCode() +
            ".lang"));
    new BufferedReader(new InputStreamReader(stream, CHARSET))
        .lines()
        .filter(line -> !(line.isEmpty() || line.charAt(0) == '#'))
        .map(line -> line.split("=", 2))
        .forEach(line -> {
          if (!TRANSLATIONS.containsKey(line[0])) {
            TRANSLATIONS.put(line[0], new HashMap<>());
          }
          TRANSLATIONS.get(line[0]).put(locale, line[1].replaceAll("\\\\n", "\n"));
        });
    LOGGER.info("Loaded translations for {}", locale.getLocaleCode());
  }

  public static int size() {
    return TRANSLATIONS.size();
  }

  /**
   * Returns all translations of the text to which the specified key is mapped.
   *
   * @param key The key whose associated translations of the text is to be returned
   * @return The translations of the text to which the specified key is mapped
   * @throws MissingTranslationException If no translations for the key exists (should not happen)
   */
  private static Map<DiscordLocale, String> getAll(String key) throws MissingTranslationException {
    Map<DiscordLocale, String> translations = TRANSLATIONS.get(key);
    if (translations == null) {
      throw new MissingTranslationException(key);
    }
    return translations;
  }

  /**
   * Returns the default translation of the text to which the specified key is mapped.
   *
   * @param key The key whose associated default translation of the text is to be returned
   * @return The default translation of the text to which the specified key is mapped
   * @throws MissingTranslationException If no default translation for the key exists (should not
   *                                     happen)
   */
  public static String getDefault(String key) throws MissingTranslationException {
    String translation = getAll(key).get(DEFAULT);
    if (translation == null) {
      throw new MissingTranslationException(key, DEFAULT);
    }
    return translation;
  }

  /**
   * Returns the translation of the text to which the specified key and locale is mapped.
   *
   * @param key    The key whose associated translation of the text is to be returned
   * @param locale The locale whose associated translation of the text is to be returned
   * @return The translation of the text to which the specified key and locale is mapped
   * @throws MissingTranslationException If either no translations (see {@link #getAll(String)}) or
   *                                     no default translation (see {@link #getDefault(String)})
   *                                     for the key exists (should both not happen)
   */
  public static String get(String key, DiscordLocale locale) throws MissingTranslationException {
    return getAll(key).getOrDefault(locale, getDefault(key));
  }

  /**
   * Performs the given action for each locale-translation-pair for the text until all pairs have
   * been processed or the action throws an exception.
   *
   * @param key    The key whose associated translations of the text is to be processed
   * @param action The action to be performed for each entry
   * @throws NullPointerException        If the specified action is null
   * @throws MissingTranslationException If no translations for the key exists (should not happen;
   *                                     see {@link #getAll(String)})
   * @see Map#forEach(BiConsumer)
   */
  public static void forEach(String key, BiConsumer<DiscordLocale, String> action)
      throws MissingTranslationException, NullPointerException {
    getAll(key).forEach(action);
  }
}
