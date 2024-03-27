package com.malex.webservice;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import com.malex.exception.RssReaderException;
import com.malex.model.RssItem;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssReaderWebService {

  /** Read the latest news on Rss url */
  public List<RssItem> readRssNews(String url) {
    try {
      var rawItems = new RssReader().read(url).toList();
      return mapRssToItems(rawItems);
    } catch (IOException e) {
      String errorMessage = String.format("Error reading RSS by url - %s", url);
      throw new RssReaderException(errorMessage, e);
    }
  }

  private List<RssItem> mapRssToItems(List<Item> rawItems) {
    return rawItems.stream() //
        .map(RssItem::new)
        .toList();
  }
}
