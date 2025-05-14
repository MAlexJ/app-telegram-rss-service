package com.malex.webservice.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationMessage(
        @JsonProperty("chat_id") Long chatId,
        String text,
        /*
         * parse_mode: HTML
         */
        @JsonProperty("parse_mode") String parse_mode) {

    public NotificationMessage(@JsonProperty("chat_id") Long chatId, String text) {
        this(chatId, text, "HTML");
    }
}
