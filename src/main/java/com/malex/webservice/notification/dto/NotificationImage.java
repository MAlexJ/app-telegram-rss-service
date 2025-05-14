package com.malex.webservice.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationImage(
        @JsonProperty("chat_id") Long chatId,
        @JsonProperty("photo") String imageUrl,
        @JsonProperty("caption") String text,
        /*
         * parse_mode: HTML
         */
        @JsonProperty("parse_mode") String parse_mode) {

    public NotificationImage(
            @JsonProperty("chat_id") Long chatId,
            @JsonProperty("photo") String imageUrl,
            @JsonProperty("caption") String text) {
        this(chatId, imageUrl, text, "HTML");
    }
}
