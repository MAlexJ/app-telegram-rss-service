package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.dto.CustomizationDto;
import com.malex.model.request.CustomizationRequest;
import com.malex.repository.CustomizationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomizationStorageService {

  private final CustomizationRepository repository;
  private final ObjectMapper mapper;

  public List<CustomizationDto> findAll() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public CustomizationDto save(CustomizationRequest request) {
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }
}
