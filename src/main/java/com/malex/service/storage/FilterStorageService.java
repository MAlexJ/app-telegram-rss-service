package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.FilterEntity;
import com.malex.model.request.FilterRequest;
import com.malex.model.response.FilterResponse;
import com.malex.repository.FilterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterStorageService {

  private final FilterRepository repository;
  private final ObjectMapper mapper;

  public List<FilterResponse> findAllFilters() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public List<FilterEntity> findAllActiveFilters() {
    return repository.findAllByActive(true);
  }

  public FilterResponse save(FilterRequest request) {
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }

  /** Inactive filter by id */
  public Integer disableFilterById(String id) {
    return repository.setInactiveFilterStatusById(id).intValue();
  }
}
