package com.luka5w.gpdiscordbot.util.commands;

import com.luka5w.gpdiscordbot.handler.LocalizationHandler;
import org.javacord.api.interaction.SlashCommandOptionType;

public class SlashCommandOptionBuilder extends
    org.javacord.api.interaction.SlashCommandOptionBuilder {


  public SlashCommandOptionBuilder(SlashCommandOptionType type, boolean required,
      String translationKeyStack, String name) {
    super();
    String descKey = translationKeyStack + ".opt." + name + ".dsc";
    this.setType(type)
        .setRequired(required)
        .setName(name)
        .setDescription(LocalizationHandler.getDefault(descKey));
    LocalizationHandler.forEach(descKey, this::addDescriptionLocalization);
  }

  public static class SlashCommandStringOptionBuilder extends SlashCommandOptionBuilder {

    public SlashCommandStringOptionBuilder(boolean required, String commandStack, String name) {
      super(SlashCommandOptionType.STRING, required, commandStack, name);
    }
  }

  public static class SlashCommandLongOptionBuilder extends SlashCommandOptionBuilder {

    public SlashCommandLongOptionBuilder(boolean required, String commandStack, String name) {
      super(SlashCommandOptionType.LONG, required, commandStack, name);
    }
  }

  public static class SlashCommandBoolOptionBuilder extends SlashCommandOptionBuilder {

    public SlashCommandBoolOptionBuilder(boolean required, String commandStack, String name) {
      super(SlashCommandOptionType.BOOLEAN, required, commandStack, name);
    }
  }

  public static class SlashCommandUserOptionBuilder extends SlashCommandOptionBuilder {

    public SlashCommandUserOptionBuilder(boolean required, String commandStack, String name) {
      super(SlashCommandOptionType.USER, required, commandStack, name);
    }
  }
}
