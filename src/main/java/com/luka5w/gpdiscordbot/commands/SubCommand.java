package com.luka5w.gpdiscordbot.commands;

import com.luka5w.gpdiscordbot.handler.LocalizationHandler;
import com.luka5w.gpdiscordbot.util.commands.MissingPermissionException;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionType;

public abstract class SubCommand implements ICommand {

  /**
   * The commands logger (intended for each subcommand too).
   */
  protected Logger logger;

  /**
   * The subcommand-group-wide unique name of the subcommand.
   */
  private final String name;

  /**
   * Available options for this subcommand.
   */
  protected List<SlashCommandOption> options;

  /**
   * Initiates the subcommand with the minimum properties.
   *
   * @param logger  The parent commands logger
   * @param name    The subcommand-group-wide unique name of the subcommand
   * @param options The available options for this subcommand
   */
  public SubCommand(Logger logger, String name, SlashCommandOption... options) {
    this.logger = logger;
    this.name = name;
    this.options = List.of(options);
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Builds the subcommand. Override to add additional properties and make a super call to add the
   * minimum required properties.
   *
   * @return a builder with the minimum required properties
   */
  public SlashCommandOptionBuilder build(String translationKeyStack) {
    translationKeyStack += ".sub." + this.name;
    String descKey = translationKeyStack + ".dsc";
    String optKey = translationKeyStack + ".opt";
    // TODO: 30.12.22 options are not really implemented well. translations are not implemented
    SlashCommandOptionBuilder builder = new SlashCommandOptionBuilder()
        .setType(SlashCommandOptionType.SUB_COMMAND)
        .setName(this.name)
        .setDescription(LocalizationHandler.getDefault(descKey));
    if (!this.options.isEmpty()) {
      builder.setOptions(this.options);
    }
    LocalizationHandler.forEach(descKey, builder::addDescriptionLocalization);
    return builder;
  }

  public abstract void exec(SlashCommandInteraction interaction)
      throws MissingPermissionException, RuntimeException;
}
