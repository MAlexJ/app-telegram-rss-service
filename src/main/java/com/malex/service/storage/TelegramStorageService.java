package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.type.ChatType;
import com.malex.model.dto.UserDto;
import com.malex.model.dto.ChatDto;
import com.malex.repository.ChatRepository;
import com.malex.repository.UserRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramStorageService {

  private final UserRepository userRepository;
  private final ChatRepository chatRepository;
  private final ObjectMapper mapper;

  public void saveChat(ChatDto chatDto) {
    if (chatDto.getType() == ChatType.PRIVATE) {
      UserDto userDto = chatDto.getUser();
      if (!userRepository.existsByUserId(userDto.userId())) {
        userRepository.save(mapper.dtoToEntity(userDto));
      }
    }
    if (!chatRepository.existsByChatId(chatDto.getChatId())) {
      chatRepository.save(mapper.dtoToEntity(chatDto));
    }
  }

  public List<UserDto> findAllUsers() {
    return userRepository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public List<ChatDto> findAllChats() {
    return chatRepository.findAll().stream().map(mapper::entityToDto).toList();
  }
}
