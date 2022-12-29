package com.luka5w.gpdiscordbot.commands;

import com.luka5w.gpdiscordbot.util.commands.MissingPermissionException;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

public interface ICommand {

  /**
   * Returns the unique name of the command: {@code /<name>}.
   *
   * @return The unique name of the command
   */
  String getName();

  /**
   * The logic of this command (not subcommands).
   *
   * @param interaction The interaction retrieved by the {@link SlashCommandCreateEvent}
   * @throws RuntimeException When anything went wrong (The command should still catch expected
   *                          exceptions).
   */
  void exec(SlashCommandInteraction interaction)
      throws MissingPermissionException, RuntimeException;
}
