package com.example.fabcarapplication.config.member;


import lombok.Data;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("admin")
@Data
public class AdminConfig {

  public String host;
  public String profile;
  public String user;
  public String secret;
  public String mspId;

  public EnrollmentRequest getEnrollmentRequest(){
    final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
    enrollmentRequestTLS.addHost("localhost");
    enrollmentRequestTLS.setProfile("tls");
    return enrollmentRequestTLS;
  }

}
