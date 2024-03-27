package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.RssTopicEntity;
import com.malex.model.response.RssTopicResponse;
import com.malex.repository.RssTopicRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssTopicStorageService {

  private final RssTopicRepository topicRepository;
  private final ObjectMapper mapper;

  public List<RssTopicResponse> findAllTopics() {
    return topicRepository.findAll().stream().map(mapper::entityToDto).toList();
  }

  /** Find all md5 topic hashes */
  public Set<String> findAllMd5Hash() {
    return topicRepository.findAll().stream() //
        .map(RssTopicEntity::getMd5Hash)
        .collect(Collectors.toSet());
  }

  /** Save new rss topic */
  public void saveNewRssTopic(RssTopicEntity entity) {
    var persistEntity = topicRepository.save(entity);
    log.info("Saved item id - {}", persistEntity.getId());
  }

  /** Find first active rss topic order by created asc */
  public Optional<RssTopicEntity> findFirstActiveRssTopic() {
    return topicRepository.findFirstByIsActiveOrderByCreatedAsc(true);
  }

  /**
   * Update RSS topic and set topic inactivity
   *
   * @param id - topic GUID id
   */
  public void setTopicInactivity(String id) {
    topicRepository.updateRssTopicEntity(id);
  }
}
