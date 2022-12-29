package com.luka5w.gpdiscordbot.util.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility methods for working with files.
 */
public class FileUtils {

  /**
   * Copies a config file from the resources to the working directory.
   *
   * @param name The name of the config file to copy
   */
  public static void copyTemplateConfigsIfMissing(String name, boolean review) {
    Logger logger = LogManager.getLogger(FileUtils.class);
    if (!new File(name).exists()) {
      try {
        copyTemplateFile(name);
        if (review) {
          logger.info("Created default configuration file ({}). Review the configuration and "
              + "start again.", new File(name).getPath());
          System.exit(0);
        } else {
          logger.info("Created default configuration file ({}). A review is not required for "
                  + "basic functionality.",
              new File(name).getPath());
        }
      } catch (IOException | NullPointerException e) {
        logger.error("Failed while creating default config file at '"
            + new File(name).getPath() + "'", e);
      }
    }
  }

  /**
   * Copies a file from the resources to the working directory.
   *
   * @param name The name of the file to copy
   * @throws IOException          If an error occurred during copying
   * @throws NullPointerException If the resource could not be found, the resource is in a package
   *                              that is not opened unconditionally, or access to the resource is
   *                              denied by the security manager.
   */
  public static void copyTemplateFile(String name) throws IOException, NullPointerException {
    Files.copy(Objects.requireNonNull(FileUtils.class.getClassLoader().getResourceAsStream(name)),
        new File(name).toPath());

  }

}
