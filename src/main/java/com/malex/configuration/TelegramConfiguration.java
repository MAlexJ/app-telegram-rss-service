package com.malex.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/*
 * Configuration class that initializes and manages the lifecycle
 * of a Telegram LongPollingBot within a Spring Boot application.
 *
 * On application start, it registers the bot with the TelegramBots API.
 * On application shutdown, it gracefully stops the bot session.
 *
 * Dependencies:
 * - spring-boot
 * - telegrambots-spring-boot-starter (or manual setup)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramConfiguration {

  /**
   * Telegram LongPollingBot injected via constructor. Your bot class must implement {@link
   * LongPollingBot}.
   */
  private final LongPollingBot bot;

  /** Reference to the bot session, required for stopping the bot on shutdown. */
  private BotSession botSession;

  /*
   * EventListener for ContextRefreshedEvent.class:
   * Event raised when an ApplicationContext gets initialized or refreshed.
   */
  @SneakyThrows
  @EventListener({ContextRefreshedEvent.class})
  public void init() {
    var botsApi = new TelegramBotsApi(DefaultBotSession.class);
    this.botSession = botsApi.registerBot(bot);
    log.info("Starting TelegramBotsApi - {}", this.botSession.isRunning());
  }

  /** Gracefully stops the Telegram bot session when the Spring context is closed. */
  @EventListener(ContextClosedEvent.class)
  public void shutdown() {
    if (this.botSession != null && this.botSession.isRunning()) {
      this.botSession.stop();
      log.info("Telegram bot session stopped.");
    }
  }
}
