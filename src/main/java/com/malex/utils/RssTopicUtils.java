package com.malex.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RssTopicUtils {

  private RssTopicUtils() {
    // not use
  }

  public static List<String> randomlyRearrangingIds(List<String> ids) {
    var list = new ArrayList<>(ids);
    Collections.shuffle(list);
    return list;
  }
}
