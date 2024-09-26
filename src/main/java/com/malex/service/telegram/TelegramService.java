package com.malex.service.telegram;

import com.malex.model.request.MessageRequest;
import com.malex.model.response.MessageResponse;
import com.malex.webservice.telegram.TelegramPublisherWebService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {

  private final TelegramPublisherWebService publisherWebService;

  public MessageResponse sendMessage(MessageRequest request) {
    var chatId = request.chatId();
    var text = request.text();
    var image = request.image();

    // todo: review logic
    var messageId =
        Optional.ofNullable(image)
            .map(img -> publisherWebService.sendMessage(chatId, img, text))
            // .or (Supplier -> sendMessage(...) )
            // .map( messageId -> new MessageResponse(messageId))
            .orElseGet(() -> publisherWebService.sendMessage(chatId, text));

    var creationDate = LocalDateTime.now();
    return new MessageResponse(messageId, creationDate);
  }
}
