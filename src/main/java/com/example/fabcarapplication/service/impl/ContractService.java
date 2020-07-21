package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.config.connection.GatewayBuilderConfig;
import com.example.fabcarapplication.config.member.UserConfig;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.example.fabcarapplication.service.WalletService;
import java.io.IOException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ContractService.class);

  @Autowired
  CertificateAuthorityService certificateAuthorityService;

  @Autowired
  WalletService walletService;

  @Autowired
  UserConfig userConfig;

  @Autowired
  GatewayBuilderConfig gatewayBuilderConfig;

  public Contract myContract;

//  @Bean
  public void ContractService() {
    initGatewayBuilderConfig();
    Gateway gatewayConnection = createConnection();
    Network myChannelNetwork = getNetwork(gatewayConnection, "mychannel");
    myContract = getContract(myChannelNetwork, "fabfund");
  }

  public void initGatewayBuilderConfig() {

    try {
      gatewayBuilderConfig.getGatewayBuilderConfig(walletService.getWallet(), "admin");
    } catch (IOException e) {
      LOGGER.error("{}",e);
    }

  }

  private Gateway createConnection() {
    return gatewayBuilderConfig.getBuilder().connect();
  }

  private void closeConnection() {
    gatewayBuilderConfig.getBuilder().connect().close();
  }

  private Network getNetwork(Gateway gateway, String channel) {
    Network network = null;
    try {
      network = gateway.getNetwork(channel);
    } catch (Exception e) {
      LOGGER.info(e.getMessage());
    }
    return network;
  }

  private Contract getContract(Network network, String chaincode) {
    return network.getContract(chaincode);
  }

  private Contract initConn() {
    initGatewayBuilderConfig();
    Gateway gatewayConnection = createConnection();
    Network myChannelNetwork = getNetwork(gatewayConnection, "mychannel");
    return getContract(myChannelNetwork, "fabfund");
  }

  public Contract getMyContract() {
    return myContract;
  }
}
