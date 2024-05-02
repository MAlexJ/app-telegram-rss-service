package com.malex.mapper;

import com.malex.model.dto.ChatDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.dto.UserDto;
import com.malex.model.entity.*;
import com.malex.model.request.*;
import com.malex.model.response.*;
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

  @Mapping(source = "active", target = "isActive")
  RssSubscriptionResponse entityToDto(SubscriptionEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(source = "isActive", target = "active")
  RssTopicEntity dtoToEntity(RssTopicDto dto);

  @Mapping(source = "active", target = "isActive")
  RssTopicResponse entityToDto(RssTopicEntity entity);

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
  @Mapping(target = "created", ignore = true)
  @Mapping(source = "isActive", target = "active")
  FilterEntity dtoToEntity(FilterRequest dto);

  @Mapping(source = "active", target = "isActive")
  FilterResponse entityToDto(FilterEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  CustomizationEntity dtoToEntity(CustomizationRequest dto);

  CustomizationResponse entityToDto(CustomizationEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  SpecialCharacterEntity dtoToEntity(SpecialCharacterRequest dto);

  SpecialCharacterResponse entityToDto(SpecialCharacterEntity entity);
}
