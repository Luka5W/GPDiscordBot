package com.luka5w.gpdiscordbot.handler;

import com.luka5w.gpdiscordbot.commands.Command;
import com.luka5w.gpdiscordbot.commands.CommandPing;
import com.luka5w.gpdiscordbot.commands.CommandStatus;
import com.luka5w.gpdiscordbot.commands.ICommand;
import com.luka5w.gpdiscordbot.commands.SubCommand;
import com.luka5w.gpdiscordbot.util.messages.EmbedType;
import com.luka5w.gpdiscordbot.util.messages.Respond;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;

/**
 * Handler class for creating, registering and executing commands.
 */
public class CommandHandler {

  private static final Logger LOGGER = LogManager.getLogger(CommandHandler.class);

  private static final Set<Command> COMMANDS = new HashSet<>();

  /**
   * Registers all ApplicationCommands (SlashCommands).
   *
   * @param api The api to register the commands to
   */
  public static void registerCommands(DiscordApi api) {
    COMMANDS.add(new CommandPing());
    COMMANDS.add(new CommandStatus());

    LOGGER.info("Registering Commands...");
    api.bulkOverwriteGlobalApplicationCommands(
        COMMANDS.stream().map(Command::build).collect(Collectors.toSet())).join();
    LOGGER.info("Registered {} Commands.", COMMANDS.size());
  }

  /**
   * EventListener - Executed when a command should be executed. Catches all exceptions thrown
   * during execution.
   *
   * @param event The event
   */
  public static void onSlashCommandCreate(SlashCommandCreateEvent event) {
    // TODO: 30.12.22 CLEAN UP!!!!11! this bot won't support nested subcommands (like initially
    //  planned).
    SlashCommandInteraction interaction = event.getSlashCommandInteraction();
    Optional<Command> oCMD = COMMANDS.stream()
        .filter(c -> Objects.equals(interaction.getCommandName(), c.getName()))
        .findFirst();
    if (oCMD.isEmpty()) {
      throw new IllegalStateException("Received SlashCommandInteraction but couldn't find command"
          + " for name '" + interaction.getCommandName() + "' (ID: " + interaction.getCommandId()
          + ")");
    }
    ICommand cmd = oCMD.get();
    SubCommand sCMD = null;
    if (!((Command) cmd).getSubCommands().isEmpty() && !cmd.getName()
        .equals(interaction.getFullCommandName())) {
      Optional<SubCommand> oSCMD = ((Command) cmd).getSubCommands().stream()
          .filter(c -> {
            Optional<SlashCommandInteractionOption> oo = interaction.getOptionByIndex(0);
            if (oo.isEmpty()) {
              return false;
            }
            SlashCommandInteractionOption o = oo.get();
            return o.isSubcommandOrGroup() && Objects.equals(o.getName(), c.getName());
          })
          .findFirst();
      if (oSCMD.isPresent()) {
        sCMD = oSCMD.get();
      } else {
        throw new IllegalStateException("Received SlashCommandInteraction but couldn't find "
            + "subcommand for name '" + interaction.getCommandName() + "' (ID: "
            + interaction.getCommandId() + ")");
      }
    }
    try {
      if (sCMD == null) {
        cmd.exec(interaction);
      } else {
        sCMD.exec(interaction);
      }
    } catch (Throwable e) {
      LOGGER.error(
          "Unexpected exception while executing command '" + interaction.getFullCommandName()
              + "' (User: '" + interaction.getUser().getDiscriminatedName() + "'):", e);

      Respond.immediate(interaction, true, EmbedType.FAILURE, "commands.failure.unknown.title",
          "commands.failure.unknown.dsc").exceptionally(
          e0 -> {
            Respond.followup(interaction, true, EmbedType.FAILURE,
                    "commands.failure.unknown.title", "commands.failure.unknown.dsc")
                .thenAccept(
                    s1 -> LOGGER.fatal("Commands must not send answers and throw exceptions."))
                .exceptionally(e1 -> {
                  LOGGER.error("Unable to notify command sender about command failure. Attempted "
                          + "to create an immediate responder: {} and followup responder: {}",
                      e0.getMessage(), e1.getMessage());
                  return null;
                });
            return null;
          });
    }
  }
}
