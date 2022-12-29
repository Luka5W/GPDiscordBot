package com.luka5w.gpdiscordbot.config;

import com.luka5w.gpdiscordbot.util.data.FileUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;

/**
 * Configuration of everything related to authorization (tokens, …).<br/> The configuration file
 * <b>must</b> exist in the working directory.
 */
public class ClientConfig {

  private static final String FILE_NAME = "client.conf";

  static {
    FileUtils.copyTemplateConfigsIfMissing(FILE_NAME, true);
  }

  private static final Config CONF = ConfigFactory.parseFile(new File(FILE_NAME));

  /**
   * The Client ID
   */
  public static final long ID = CONF.getLong("client.id");

  /**
   * The Client Secret
   */
  public static final String SECRET = CONF.getString("client.secret");

  /**
   * The Bot Token
   */
  public static final String TOKEN = CONF.getString("client.bot_token");
}
