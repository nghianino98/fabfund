package com.example.fabcarapplication.config.connection;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("ccp")
@Data
public class NetworkConfig {

  String networkConfigPath;

  Path getPath() {
    return Paths.get(networkConfigPath);
  }

}
