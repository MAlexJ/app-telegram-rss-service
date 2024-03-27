package com.malex.model.dto;

import com.malex.model.type.ChatType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@NoArgsConstructor
public class ChatDto {

  private Long chatId;
  private String title;
  private ChatType type;
  private UserDto user;
  /* true by default */
  private boolean isActive;

  public ChatDto(Chat chat) {
    this.chatId = chat.getId();
    this.title = chat.getTitle();
    this.type = ChatType.findType(chat.getType());
    this.isActive = true;
  }

  public ChatDto(Message message) {
    this(message.getChat());
    this.user = new UserDto(message.getFrom());
  }
}
