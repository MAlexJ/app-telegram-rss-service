package com.malex.webservice.telegram;

import static com.malex.utils.MessageFormatUtils.shortMessage;

import com.malex.exception.telegram.TelegramPublisherException;
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

  /* Send simple message
   *
   * @throws TelegramPublisherException exception
   */
  public Integer sendMessage(Long chatId, String text) {
    try {
      var message = publishTextMessage(chatId, text);
      return message.getMessageId();
    } catch (TelegramApiException ex) {
      throw new TelegramPublisherException(ex);
    } finally {
      logMessage(chatId, text);
    }
  }

  /*
   * Send message with image and text
   *
   * @throws TelegramPublisherException exception
   */
  public Integer sendMessage(Long chatId, String image, String text) {
    try {
      var message = publishMediaMessage(chatId, image, text);
      return message.getMessageId();
    } catch (TelegramApiException ex) {
      throw new TelegramPublisherException(ex);
    } finally {
      logMessage(chatId, text);
    }
  }

  private Message publishTextMessage(Long chatId, String text) throws TelegramApiException {
    return sender.execute(
        SendMessage.builder()
            .chatId(chatId)
            .parseMode(ParseMode.HTML)
            .text(text)
            .protectContent(true)
            .build());
  }

  private Message publishMediaMessage(Long chatId, String image, String text)
      throws TelegramApiException {
    return sender.execute(
        SendPhoto.builder()
            .chatId(chatId)
            .parseMode(ParseMode.HTML)
            .photo(new InputFile(image))
            // todo: SendPhoto query: [400] Bad Request: message caption is too long
            .caption(text)
            .protectContent(true)
            .build());
  }

  private void logMessage(Long chatId, String text) {
    log.info("Publish RSS topic to chat_id - '{}', text - {}", chatId, shortMessage(text, 100));
  }
}
