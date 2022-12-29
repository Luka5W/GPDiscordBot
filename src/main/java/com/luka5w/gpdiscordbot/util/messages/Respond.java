package com.luka5w.gpdiscordbot.util.messages;

import com.luka5w.gpdiscordbot.handler.LocalizationHandler;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionMessageBuilderBase;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import org.jetbrains.annotations.Nullable;

public class Respond {

  public static EmbedBuilder createEmbed(
      SlashCommandInteraction interaction, EmbedType type, String titleKey,
      @Nullable String descKey, @Nullable List<EmbedField> fields) {
    DiscordLocale locale = interaction.getLocale();
    EmbedBuilder eBuilder = new com.luka5w.gpdiscordbot.util.messages.EmbedBuilder(type,
        LocalizationHandler.get(titleKey, locale));
    if (descKey != null) {
      eBuilder.setDescription(LocalizationHandler.get(descKey, locale));
    }
    if (fields != null && !fields.isEmpty()) {
      fields.forEach(f -> eBuilder.addField(f.getName(), f.getValue(), f.isInline()));
    }
    return eBuilder;
  }

  public static <T extends InteractionMessageBuilderBase<?>> T createMessage(
      T rBuilder,
      boolean ephemeral, EmbedBuilder... eBuilders) {
    for (org.javacord.api.entity.message.embed.EmbedBuilder eBuilder : eBuilders) {
      rBuilder.addEmbed(eBuilder);
    }
    if (ephemeral) {
      rBuilder.setFlags(MessageFlag.EPHEMERAL);
    }
    return rBuilder;
  }

  public static CompletableFuture<InteractionOriginalResponseUpdater> immediate(
      SlashCommandInteraction interaction, boolean ephemeral, EmbedType type,
      String titleKey) {
    return immediate(interaction, ephemeral, type, titleKey, null);
  }

  public static CompletableFuture<InteractionOriginalResponseUpdater> immediate(
      SlashCommandInteraction interaction, boolean ephemeral, EmbedType type,
      String titleKey, @Nullable String descKey, EmbedField... fields) {
    return Respond.createMessage(interaction.createImmediateResponder(), ephemeral,
        Respond.createEmbed(interaction, type, titleKey, descKey, List.of(fields))).respond();
  }

  public static CompletableFuture<InteractionOriginalResponseUpdater> immediate(
      SlashCommandInteraction interaction, boolean ephemeral, EmbedType type,
      String titleKey, @Nullable String descKey, @Nullable List<EmbedField> fields) {
    return Respond.createMessage(interaction.createImmediateResponder(), ephemeral,
        createEmbed(interaction, type, titleKey, descKey, fields)).respond();
  }

  public static CompletableFuture<Message> followup(SlashCommandInteraction interaction,
      boolean ephemeral, EmbedType type,
      String titleKey) {
    return followup(interaction, ephemeral, type, titleKey, null);
  }

  public static CompletableFuture<Message> followup(SlashCommandInteraction interaction,
      boolean ephemeral, EmbedType type,
      String titleKey, @Nullable String descKey) {
    return followup(interaction, ephemeral, type, titleKey, descKey, null);
  }

  public static CompletableFuture<Message> followup(SlashCommandInteraction interaction,
      boolean ephemeral, EmbedType type,
      String titleKey, @Nullable String descKey, @Nullable List<EmbedField> fields) {
    return Respond.createMessage(interaction.createFollowupMessageBuilder(), ephemeral,
        createEmbed(interaction, type, titleKey, descKey, fields)).send();
  }

  public static void update(SlashCommandInteraction interaction,
      InteractionOriginalResponseUpdater updater, EmbedType type, String titleKey) {
    update(interaction, updater, type, titleKey, null);
  }

  public static void update(SlashCommandInteraction interaction,
      InteractionOriginalResponseUpdater updater, EmbedType type, String titleKey,
      @Nullable String descKey) {
    update(interaction, updater, type, titleKey, descKey, null);
  }

  public static CompletableFuture<Message> update(SlashCommandInteraction interaction,
      InteractionOriginalResponseUpdater updater, EmbedType type, String titleKey,
      @Nullable String descKey, @Nullable List<EmbedField> fields) {
    return createMessage(updater, false,
        createEmbed(interaction, type, titleKey, descKey, fields)).update();
  }
}
