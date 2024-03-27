package com.malex.service;

import com.malex.exception.Md5HashCalculationException;
import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class Md5HashService {

  private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";

  public String md5HashCalculation(String... values) {
    try {
      String join =
          Arrays.stream(values)
              .map(val -> Optional.ofNullable(val).orElse(MESSAGE_DIGEST_ALGORITHM))
              .collect(Collectors.joining());
      MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
      md.update(join.getBytes());
      return DatatypeConverter.printHexBinary(md.digest());
    } catch (NoSuchAlgorithmException e) {
      String errorMessage =
          String.format("Error of MD5 hash gender by values - %s", Arrays.toString(values));
      throw new Md5HashCalculationException(errorMessage, e);
    }
  }
}
