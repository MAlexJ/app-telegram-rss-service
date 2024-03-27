package com.malex.model.entity;

import com.malex.model.dto.UserDto;
import com.malex.model.type.ChatType;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "chats")
@TypeAlias("ChatEntity")
public class ChatEntity {

  @MongoId private String id;

  @Indexed(unique = true)
  private Long chatId;

  @Field(name = "active")
  private boolean isActive;

  private String title;
  private ChatType type;

  private UserDto user;
}
