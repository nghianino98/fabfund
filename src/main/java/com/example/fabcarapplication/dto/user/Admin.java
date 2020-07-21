package com.example.fabcarapplication.dto.user;

import java.security.PrivateKey;
import java.util.Set;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class Admin implements User {

  private Identity adminIdentity;

  public Admin(Identity adminIdentity) {
    this.adminIdentity = adminIdentity;
  }

  @Override
  public String getName() {
    return "admin";
  }

  @Override
  public Set<String> getRoles() {
    return null;
  }

  @Override
  public String getAccount() {
    return null;
  }

  @Override
  public String getAffiliation() {
    return "org1.department1";
  }

  @Override
  public Enrollment getEnrollment() {
    return new Enrollment() {

      @Override
      public PrivateKey getKey() {
        return adminIdentity.getPrivateKey();
      }

      @Override
      public String getCert() {
        return adminIdentity.getCertificate();
      }
    };
  }

  @Override
  public String getMspId() {
    return "Org1MSP";
  }
}
