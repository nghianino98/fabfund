package com.example.fabcarapplication.service;

import java.io.IOException;
import java.nio.file.Paths;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;

public interface WalletService {

  Wallet getWallet();

  Wallet createFileSystemWallet();

  boolean checkExist(String label);

  boolean put(String label, Identity identity) throws IOException;

  Identity get(String label) throws IOException;

}
