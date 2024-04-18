package com.malex.mapper;

import com.malex.model.dto.ChatDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.dto.UserDto;
import com.malex.model.entity.*;
import com.malex.model.request.FilterRequest;
import com.malex.model.request.RssSubscriptionRequest;
import com.malex.model.request.TemplateRequest;
import com.malex.model.response.FilterResponse;
import com.malex.model.response.RssSubscriptionResponse;
import com.malex.model.response.RssTopicResponse;
import com.malex.model.response.TemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** MapStruct mapper: */
@Mapper(componentModel = "spring")
public interface ObjectMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "lastModified", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(source = "isActive", target = "active")
  SubscriptionEntity dtoToEntity(RssSubscriptionRequest dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(source = "isActive", target = "active")
  RssTopicEntity dtoToEntity(RssTopicDto dto);

  @Mapping(source = "active", target = "isActive")
  RssSubscriptionResponse entityToDto(SubscriptionEntity entity);

  @Mapping(source = "active", target = "isActive")
  RssTopicResponse entityToDto(RssTopicEntity entity);

  ChatDto entityToDto(ChatEntity entity);

  UserDto entityToDto(UserEntity entity);

  @Mapping(target = "id", ignore = true)
  ChatEntity dtoToEntity(ChatDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  UserEntity dtoToEntity(UserDto dto);

  @Mapping(source = "isActive", target = "active")
  TemplateEntity dtoToEntity(TemplateRequest dto);

  @Mapping(source = "active", target = "isActive")
  TemplateResponse entityToDto(TemplateEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "isActive", target = "active")
  FilterEntity dtoToEntity(FilterRequest dto);

  @Mapping(source = "active", target = "isActive")
  FilterResponse entityToDto(FilterEntity entity);
}
