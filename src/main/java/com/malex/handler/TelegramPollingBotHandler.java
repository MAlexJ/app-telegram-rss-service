package com.malex.handler;

import com.malex.model.dto.ChatDto;
import com.malex.service.storage.TelegramStorageService;
import com.malex.utils.MessageFormatUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.*;

@Slf4j
@Service
public class TelegramPollingBotHandler extends InitStatePollingHandler {

  private final TelegramStorageService storageService;

  @Value("${telegram.bot.username}")
  private String username;

  public TelegramPollingBotHandler(
      @Value("${telegram.bot.token}") String token, TelegramStorageService storageService) {
    super(token);
    this.storageService = storageService;
  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      var message = update.getMessage();
      var user = message.getFrom();
      var chat = message.getChat();
      var text = message.getText();

      /*
       * Handle private conversation with bot
       */
      if (message.isUserMessage()) {

        /*
         * Handle init private conversation
         */
        Optional.of(message)
            .filter(Message::isCommand)
            .filter(msg -> START_STATE.equals(msg.getText()))
            .map(this::initState)
            .ifPresent(msg -> storageService.saveChat(new ChatDto(message)));

        /*
         * Handle private message to bot
         */
        Optional.of(message)
            .filter(msg -> !msg.isCommand())
            .ifPresent(
                msg ->
                    log.info("Private message - '{}' from user: '{}'", text, user.getFirstName()));
      }

      /*
       * Handle group message
       */
      Optional.of(message)
          .filter(Message::isSuperGroupMessage)
          .ifPresent(
              msg ->
                  log.info(
                      "Super group message: '{}', to '{}' '{}' chat, from user: '{}'",
                      MessageFormatUtils.shortMessage(text, 100),
                      chat.getType(),
                      chat.getTitle(),
                      user.getFirstName()));
      return;
    }

    /*
     * Interaction wit telegram bot in private chanel
     */
    if (update.hasCallbackQuery()) {
      privateChatCallback(update);
      return;
    }

    /*
     * Handle initial initialization of telegram bot in the group or channel
     */
    if (update.hasMyChatMember()) {
      Optional.ofNullable(update.getMyChatMember())
          .map(ChatMemberUpdated::getChat)
          .ifPresent(
              chat -> {
                log.info(
                    "Init new '{}' chat with 'id' - {} and title - '{}'",
                    chat.getType(),
                    chat.getId(),
                    chat.getTitle());
                storageService.saveChat(new ChatDto(chat));
              });
      return;
    }

    /*
     * handle chat (group or channel) message
     */
    if (update.hasChannelPost()) {
      Optional.ofNullable(update.getChannelPost())
          .ifPresent(
              message -> {
                var chat = message.getChat();
                log.info(
                    "Chat message: '{}', from '{}' chanel '{}'",
                    message.getText(),
                    chat.getType(),
                    chat.getTitle());
              });
      return;
    }
    log.warn("unknown command or query - {}", update);
  }
}
