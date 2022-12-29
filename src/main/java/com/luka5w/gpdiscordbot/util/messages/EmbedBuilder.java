package com.luka5w.gpdiscordbot.util.messages;

public class EmbedBuilder extends org.javacord.api.entity.message.embed.EmbedBuilder {

  public EmbedBuilder(EmbedType type, String title) {
    super();
    this.setColor(type.color)
        .setTitle(title);
  }

  public EmbedBuilder(EmbedType type, String title, String description) {
    this(type, title);
    this.setColor(type.color)
        .setTitle(title)
        .setDescription(description);
  }
}
