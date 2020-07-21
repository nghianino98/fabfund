package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.user.Admin;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.springframework.stereotype.Service;

public interface CertificateAuthorityService {

  HFCAClient initHFCAClient();

  void setCryptoSuite ();

  Enrollment enroll(String user, String secret, EnrollmentRequest enrollmentRequest)
      throws EnrollmentException, InvalidArgumentException;

  Enrollment register(RegistrationRequest registrationRequest, Admin admin, String user);

}
