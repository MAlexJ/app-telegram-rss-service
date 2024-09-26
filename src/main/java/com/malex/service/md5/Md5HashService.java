package com.malex.service.md5;

import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Md5HashService {

  private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";
  private static final String DEFAULT_MD5_HASH = "DEFAULT_MD5_HASH";

  @Value("${md5Hash.calculation.baseOn.link}")
  private boolean calculateMd5HashBaseOnLink;

  public String calculateMd5HashByCriteria(String link, String title, String description) {
    if (calculateMd5HashBaseOnLink) {
      return md5HashCalculation(link);
    }
    return md5HashCalculation(title, description);
  }

  private String md5HashCalculation(String... values) {
    try {
      String join =
          Arrays.stream(values)
              .map(val -> Optional.ofNullable(val).orElse(DEFAULT_MD5_HASH))
              .collect(Collectors.joining());
      MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
      md.update(join.getBytes());
      return DatatypeConverter.printHexBinary(md.digest());
    } catch (NoSuchAlgorithmException e) {
      log.error(
          "Error of MD5 hash gender by values - {}, error -{}",
          Arrays.toString(values),
          e.getMessage());
      return DEFAULT_MD5_HASH;
    }
  }
}
