package com.example.fabcarapplication.config;

import com.example.fabcarapplication.config.connection.NetworkConfig;
import com.example.fabcarapplication.service.AdminService;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.example.fabcarapplication.service.WalletService;
import com.example.fabcarapplication.service.impl.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {

  @Autowired
  CertificateAuthorityService certificateAuthorityService;

  @Autowired
  WalletService walletService;

  @Autowired
  NetworkConfig networkConfig;

  @Autowired
  AdminService adminService;

  @Autowired
  ContractService contractService;

//  @Autowired
//  AdminController adminController;

  @Bean
  public void setStaticConfig(){
    System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
  }

  @Bean
  public void earlyStage() {
    certificateAuthorityService.initHFCAClient();

    certificateAuthorityService.setCryptoSuite();

    walletService.createFileSystemWallet();

    adminService.enrollAdmin();

    //default
    adminService.registerUser("vng-corp", "admin");

    adminService.registerUser("vin-group", "admin");

    adminService.registerUser("vina-capital", "admin");

    adminService.registerUser("user-demo", "admin");

    contractService.ContractService();

//    adminController.registerAdmin();
//
//    adminController.registerUser();

  }



}
