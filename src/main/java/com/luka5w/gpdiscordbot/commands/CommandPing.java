package com.luka5w.gpdiscordbot.commands;

import com.luka5w.gpdiscordbot.util.messages.EmbedType;
import com.luka5w.gpdiscordbot.util.messages.Respond;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * A simple command to check if the bot responses.
 */
public class CommandPing extends Command {

  public CommandPing() {
    super("ping", true, true);
  }

  @Override
  public void exec(SlashCommandInteraction interaction) {
    Respond.immediate(interaction, true, EmbedType.INFO, "cmd.ping.msg.success");
  }
}
