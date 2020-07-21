package com.example.fabcarapplication.config.connection;

import java.io.IOException;
import java.nio.file.Path;
import lombok.Data;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class GatewayBuilderConfig {

  @Autowired
  NetworkConfig networkConfig;

  Gateway.Builder builder;

  public void getGatewayBuilderConfig(Wallet wallet, String user) throws IOException {
    this.builder = Gateway.createBuilder();
    Path networkConfigPath = networkConfig.getPath();
    builder.identity(wallet,user).networkConfig(networkConfigPath)
        .discovery(true);
  }
}
