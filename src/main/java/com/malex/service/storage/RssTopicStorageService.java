package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.RssTopicEntity;
import com.malex.model.response.RssTopicResponse;
import com.malex.repository.RssTopicRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    return topicRepository.findAll().stream() //
        .map(mapper::entityToDto)
        .toList();
  }

  /** Save new rss topic */
  public void saveRssTopic(RssTopicDto dto) {
    var entity = mapper.dtoToEntity(dto);
    var persistEntity = topicRepository.save(entity);
    log.info("Saved item id - {}", persistEntity.getId());
  }

  /** Find first active rss topic order by created asc */
  public Optional<RssTopicEntity> findFirstActiveTopicBySubscriptionIdOrderByCreatedAsc(
      String subscriptionId) {
    return topicRepository.findFirstByIsActiveAndSubscriptionIdOrderByCreatedAsc(
        true, subscriptionId);
  }

  /**
   * Update RSS topic and set topic inactivity
   *
   * @param id - topic GUID id
   * @param messageId - telegram offset message id
   */
  public void deactivateRssTopics(String id, Integer messageId) {
    log.info("Deactivate Rss topic by id - {}, message id - {}", id, messageId);
    topicRepository.updateRssTopicEntity(id, messageId);
  }

  public void deactivateRssTopics(String id) {
    log.info("Deactivate Rss topic by id - {}", id);
    topicRepository.updateRssTopicEntity(id);
  }

  public boolean isNotExistTopicByMd5Hash(String md5Hash) {
    var topics = topicRepository.findRssTopicEntitiesByMd5Hash(md5Hash);
    if (topics.size() > 1) {
      log.warn("Found more that one rss topic by md5Hash - '{}'", md5Hash);
    }
    return topics.isEmpty();
  }

  public List<String> findAllTopicIdsBeforeDate(LocalDateTime dateTime) {
    return topicRepository.findAllByCreatedBefore(dateTime).stream()
        .map(RssTopicEntity::getId)
        .toList();
  }

  public void removeTopicById(String id) {
    topicRepository.deleteById(id);
  }
}
