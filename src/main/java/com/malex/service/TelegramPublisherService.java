package com.malex.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramPublisherService {

  private final DefaultAbsSender sender;

  public Optional<Message> postMessage(Long chatId, String text) {
    try {
      return Optional.of(post(chatId, text))
          .map(
              message -> {
                log.info(
                    "Publish RSS topic to telegram: message_id - {}, chat_id - {}, text - {}",
                    message.getMessageId(),
                    chatId,
                    text);
                return message;
              });
    } catch (TelegramApiException ex) {

      // https://stackoverflow.com/questions/61224362/telegram-bot-cant-find-end-of-the-entity-starting-at-truncated
      log.error(
          "Telegram Api error: chat_id - {}, text - {}, error - {}", chatId, text, ex.getMessage());
      return Optional.empty();
    }
  }

  private Message post(Long chatId, String text) throws TelegramApiException {
    return sender.execute(
        SendMessage.builder()
            .protectContent(true)
            .chatId(chatId)
            .parseMode(ParseMode.MARKDOWN)
            .text(text)
            .build());
  }
}
