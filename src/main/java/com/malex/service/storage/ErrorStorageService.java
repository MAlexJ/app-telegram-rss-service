package com.malex.service.storage;

import com.malex.model.entity.ErrorEntity;
import com.malex.repository.ErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorStorageService {

  private final ErrorRepository repository;

  public void saveError(String errorMessage, String details) {
    var errorEntity = new ErrorEntity();
    errorEntity.setErrorMessage(errorMessage);
    errorEntity.setDetails(details);
    repository.save(errorEntity);
  }
}
