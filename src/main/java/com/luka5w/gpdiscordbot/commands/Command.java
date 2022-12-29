package com.luka5w.gpdiscordbot.commands;

import com.luka5w.gpdiscordbot.handler.LocalizationHandler;
import com.luka5w.gpdiscordbot.util.commands.MissingPermissionException;
import java.util.ArrayList;
import java.util.List;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * The class every ApplicationCommand (SlashCommand) must extend to.
 */
public abstract class Command implements ICommand {

  /**
   * The unique name of the command: {@code /<name>}
   */
  private final String name;

  /**
   * Whether the command is enabled in direct message channels.
   */
  private final boolean enabledInDms;

  /**
   * Whether the command should be enabled for everyone in the guild by default.
   */
  private final boolean enabledForEveryone;

  /**
   * Makes this command enabled by default for members with one of those permissions.
   */
  private final PermissionType[] enabledFor;

  /**
   * A possibly empty list of all subcommands.
   */
  private final List<SubCommand> subCommands;

  /**
   * Initiates the command with the minimum properties.
   *
   * @param name               The unique name of the command: {@code /<name>}
   * @param enabledInDms       Whether the command is enabled in direct message channels
   * @param enabledForEveryone Whether the command should be enabled for everyone in the guild by
   *                           default.
   * @param enabledFor         Makes this command enabled by default for members with one of those
   *                           permissions.
   */
  protected Command(String name, boolean enabledInDms,
      boolean enabledForEveryone, PermissionType... enabledFor) {
    this.name = name;
    this.enabledInDms = enabledInDms;
    this.enabledForEveryone = enabledForEveryone;
    this.enabledFor = enabledFor;
    this.subCommands = new ArrayList<>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a new subcommand.
   *
   * @param subCommand The command to add
   * @return This command (e.g. for adding multiple subcommands)
   */
  protected Command addSubCommand(SubCommand subCommand) {
    this.subCommands.add(subCommand);
    return this;
  }

  /**
   * Returns all SubCommands of this command.
   *
   * @return All SubCommands of this command
   */
  public List<SubCommand> getSubCommands() {
    return this.subCommands;
  }

  /**
   * Builds the command. Override to add additional properties and make a super call to add the
   * minimum required properties.
   *
   * @return a builder with the minimum required properties
   */
  public SlashCommandBuilder build() {
    String translationKeyStack = "cmd." + this.name;
    String descKeyStack = translationKeyStack + ".dsc";
    SlashCommandBuilder builder = new SlashCommandBuilder()
        .setName(this.name)
        .setEnabledInDms(this.enabledInDms)
        .setDescription(LocalizationHandler.getDefault(descKeyStack));
    LocalizationHandler.forEach(descKeyStack, builder::addDescriptionLocalization);
    if (this.enabledForEveryone) {
      builder.setDefaultEnabledForEveryone();
    } else {
      builder.setDefaultDisabled();
    }
    if (this.enabledFor.length != 0) {
      builder.setDefaultEnabledForPermissions(this.enabledFor);
    }
    if (!this.subCommands.isEmpty()) {
      this.subCommands.forEach(s -> builder.addOption(s.build(translationKeyStack).build()));
    }
    return builder;
  }

  public abstract void exec(SlashCommandInteraction interaction)
      throws MissingPermissionException, RuntimeException;
}
