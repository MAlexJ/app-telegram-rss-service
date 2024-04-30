package com.malex.webservice;

import static com.malex.utils.MessageFormatUtils.shortMessage;

import com.malex.exception.TelegramPublisherException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramPublisherWebService {

  private final DefaultAbsSender sender;

  /** Send message with image and text */
  public Integer sendMessage(Long chatId, String image, String text) {
    try {
      var message =
          Objects.isNull(image)
              ? postSimpleMessage(chatId, text)
              : postMediaMessage(chatId, image, text);
      return message.getMessageId();
    } catch (TelegramApiException ex) {
      throw new TelegramPublisherException(ex);
    } finally {
      log.info("Publish RSS topic to chat_id - '{}', text - {}", chatId, shortMessage(text));
    }
  }

  private Message postSimpleMessage(Long chatId, String text) throws TelegramApiException {
    return sender.execute(
        SendMessage.builder()
            .protectContent(true)
            .chatId(chatId)
            .parseMode(ParseMode.HTML)
            .text(text)
            .build());
  }

  private Message postMediaMessage(Long chatId, String image, String text)
      throws TelegramApiException {
    return sender.execute(
        SendPhoto.builder()
            .chatId(chatId)
            .parseMode(ParseMode.HTML)
            .photo(new InputFile(image))
            // SendPhoto query: [400] Bad Request: message caption is too long
            .caption(text)
            .protectContent(true)
            .build());
  }
}
