package com.luka5w.gpdiscordbot.commands;

import com.luka5w.gpdiscordbot.Main;
import com.luka5w.gpdiscordbot.handler.LocalizationHandler;
import com.luka5w.gpdiscordbot.util.GitHub;
import com.luka5w.gpdiscordbot.util.TimeUtils;
import com.luka5w.gpdiscordbot.util.messages.EmbedType;
import com.luka5w.gpdiscordbot.util.messages.Respond;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.core.entity.message.embed.EmbedFieldImpl;

/**
 * A simple command to check if the bot responses.
 */
public class CommandStatus extends Command {

  private static final Logger LOGGER = LogManager.getLogger(CommandStatus.class);

  public CommandStatus() {
    super("status", true, true);
  }

  @Override
  public void exec(SlashCommandInteraction interaction) {
    interaction.respondLater(true).thenAccept(updater -> {
      DiscordLocale locale = interaction.getLocale();
      String contributors;
      try {
        contributors = GitHub.fetchContributors(
            "Luka5W/GPDiscordBot");
      } catch (IOException e) {
        LOGGER.error(e);
        contributors = LocalizationHandler.get("cmd.status.msg.success.field.contributors.error",
            locale);
      }
      List<EmbedField> fields = new java.util.ArrayList<>(List.of(
          new EmbedFieldImpl(
              LocalizationHandler.get("cmd.status.msg.success.field.repo.name", locale),
              "https://github.com/Luka5W/GPDiscordBot",
              false),
          new EmbedFieldImpl(
              LocalizationHandler.get("cmd.status.msg.success.field.contributors.name", locale),
              contributors,
              false),
          new EmbedFieldImpl(
              LocalizationHandler.get("cmd.status.msg.success.field.tech.name", locale),
              LocalizationHandler.get("cmd.status.msg.success.field.tech.value", locale)
                  .replaceAll("%b", TimeUtils.getString(Main.getBuildDate(), locale))
                  .replaceAll("%j", Main.getCompileVersion())
                  .replaceAll("%v", Main.getVersion()),
              true),
          new EmbedFieldImpl(
              LocalizationHandler.get("cmd.status.msg.success.field.system.name", locale),
              LocalizationHandler.get("cmd.status.msg.success.field.system.value", locale)
                  .replaceAll("%u", TimeUtils.durationToString(TimeUtils.getVMUpTime()))
                  .replaceAll("%s", TimeUtils.getString(TimeUtils.getVMStartTime(), locale)),
              true)
      ));
      if (interaction.getServer().isPresent()) {
        Server server = interaction.getServer().get();
        fields.add(new EmbedFieldImpl(
            LocalizationHandler.get("cmd.status.msg.success.field.server.name", locale),
            LocalizationHandler.get("cmd.status.msg.success.field.server.value", locale)
                .replaceAll("%m", String.valueOf(server.getMemberCount()))
                .replaceAll("%M", String.valueOf(server.getMaxMembers().orElse(-1)))
                .replaceAll("%b", String.valueOf(server.getBoostLevel().getId())),
            true));
      }
      Respond.update(interaction, updater, EmbedType.INFO, "cmd.status.msg.success.title", null,
              fields);
    });
  }
}
