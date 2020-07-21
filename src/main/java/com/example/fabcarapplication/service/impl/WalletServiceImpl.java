package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.config.WalletConfig;
import com.example.fabcarapplication.service.WalletService;
import java.io.IOException;
import java.nio.file.Paths;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(WalletServiceImpl.class);

  @Autowired
  WalletConfig walletConfig;

  public static Wallet wallet;

  @Override
  public Wallet getWallet() {
    return wallet;
  }

  @Override
  public Wallet createFileSystemWallet() {

    try {
      wallet = Wallet.createFileSystemWallet(Paths.get(walletConfig.getPath()));
      return wallet;
    } catch (IOException e) {
      LOGGER.error("{}",e);
      return null;

    }


  }

  @Override
  public boolean checkExist(String label) {
    try {
      return wallet.exists(label);
    } catch (IOException e) {
      LOGGER.error("{}",e);
      return false;
    }
  }

  @Override
  public boolean put(String label, Identity identity) throws IOException {

    try {
      wallet.put(label, identity);
    } catch (IOException e) {
      LOGGER.error("{}",e);
    }

    return true == checkExist(label);

  }

  @Override
  public Identity get(String label) throws IOException {
    return wallet.get(label);
  }
}
