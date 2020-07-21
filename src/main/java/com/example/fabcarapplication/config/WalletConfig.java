package com.example.fabcarapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wallet")
@Data
public class WalletConfig {

  public String path;

}
