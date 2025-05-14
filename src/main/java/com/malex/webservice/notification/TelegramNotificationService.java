package com.malex.webservice.notification;

import com.malex.webservice.notification.dto.NotificationImage;
import com.malex.webservice.notification.dto.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TelegramNotificationService {

  private final RestClient restClient;

  private final String pathTextNotifications;
  private final String pathImageNotifications;

  public TelegramNotificationService(
      @Value("${telegram.bot.url.host}") String hostUrl,
      @Value("${telegram.bot.token}") String telegramBotToken,
      @Value("${telegram.bot.url.message}") String textPath,
      @Value("${telegram.bot.url.image}") String imagePath,
      RestClient.Builder builder) {
    var url = hostUrl.formatted(telegramBotToken);
    log.info("Telegram notification webservice initialized with url - {}", url);
    this.restClient = builder.baseUrl(url).build();
    this.pathTextNotifications = textPath;
    this.pathImageNotifications = imagePath;
  }

  public void sendNotifications(Long chatId, String message) {
    try {
      restClient
          .post()
          .uri(pathTextNotifications)
          .body(new NotificationMessage(chatId, message))
          .retrieve()
          .toBodilessEntity();
      log.info("Notification sent to chat ID: {} ", chatId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void sendNotifications(Long chatId, String imageUrl, String text) {
    try {
      restClient
          .post()
          .uri(pathImageNotifications)
          .body(new NotificationImage(chatId, imageUrl, text))
          .retrieve()
          .toBodilessEntity();
      log.info("Send image notification for chat ID: {} ", chatId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
