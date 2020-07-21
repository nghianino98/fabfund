package com.example.fabcarapplication.config.member;

import lombok.Data;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("user")
@Data
public class UserConfig {

  String id;
  String affiliation;
  String mspId;

  public RegistrationRequest getRegistrationRequest(String userId) throws Exception {
    RegistrationRequest registrationRequest = new RegistrationRequest(userId);
    registrationRequest.setAffiliation(affiliation);
    registrationRequest.setEnrollmentID(userId);
    return registrationRequest;
  }

}
