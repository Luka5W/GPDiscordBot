package com.luka5w.gpdiscordbot.config;

import com.luka5w.gpdiscordbot.util.data.FileUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;

/**
 * Configuration of everything related to the behaviour of the bot or the application.
 */
public class BotConfig {

  private static final String FILE_NAME = "bot.conf";

  static {
    FileUtils.copyTemplateConfigsIfMissing(FILE_NAME, false);
  }

  private static final Config CONF = ConfigFactory.parseFile(new File(FILE_NAME))
      .withFallback(ConfigFactory.parseResources(FILE_NAME)).resolve();

  /**
   * Config values related to the logger.
   */
  public static class Logger {

    /**
     * The minimum log level.
     */
    public static final String LEVEL = CONF.getString("bot.logger.level");
  }

  /**
   * Config values related to file and directory paths.
   */
  public static class Paths {
  }

}
