package com.malex.controller.info;

import java.time.Instant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppInfoRestController {

  @Value("${application.version}")
  private String gitVersion;

  /**
   * Provide build-related information such as group and artifact: "name" - name of the project
   * "time" - timestamp of the build "version" - version of the project "group" - groupId "artifact"
   * - artifactId of the project
   */
  private final BuildProperties buildProperties;

  @GetMapping("/v1/info")
  public ResponseEntity<AppProperties> appInfo() {
    var appProperties = new AppProperties(buildProperties, gitVersion);
    return ResponseEntity.ok(appProperties);
  }

  @Data
  public static class AppProperties {

    private String gitVersion;

    private String group;

    private String version;

    private String name;

    private String artifact;

    private Instant time;

    AppProperties(BuildProperties buildProperties, String gitVersion) {
      this.group = buildProperties.getGroup();
      this.version = buildProperties.getVersion();
      this.name = buildProperties.getName();
      this.artifact = buildProperties.getArtifact();
      this.time = buildProperties.getTime();
      this.gitVersion = gitVersion;
    }
  }
}
