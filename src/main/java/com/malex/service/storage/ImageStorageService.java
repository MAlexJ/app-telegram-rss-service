package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.dto.ImageDto;
import com.malex.model.request.ImageRequest;
import com.malex.repository.ImageRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

  private final ImageRepository repository;
  private final ObjectMapper mapper;

  public List<ImageDto> findAll() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public Optional<ImageDto> findById(String id) {
    return repository.findById(id).map(mapper::entityToDto);
  }

  public ImageDto save(ImageRequest request) {
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }
}
