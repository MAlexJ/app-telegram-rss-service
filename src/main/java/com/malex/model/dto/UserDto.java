package com.malex.model.dto;

import com.malex.utils.MessageFormatUtils;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.objects.User;

public record UserDto(Long userId, String firstName, String lastName, String username) {

  public UserDto(User user) {
    this(
        Objects.requireNonNull(user.getId(), MessageFormatUtils.errorMessage("Telegram user id")),
        user.getFirstName(),
        user.getLastName(),
        user.getUserName());
  }
}
