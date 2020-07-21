package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.config.connection.CertificateAuthorityConfig;
import com.example.fabcarapplication.dto.user.Admin;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateAuthorityServiceImpl implements CertificateAuthorityService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CertificateAuthorityServiceImpl.class);

  @Autowired
  CertificateAuthorityConfig certificateAuthorityConfig;


  private HFCAClient hfcaClient;

  @Override
  public HFCAClient initHFCAClient() {

    try {
      hfcaClient = HFCAClient.createNewInstance(certificateAuthorityConfig.getUrl(),
          certificateAuthorityConfig.getProps());
      return hfcaClient;
    } catch (MalformedURLException e) {
      LOGGER.error("{}",e);
      return null;
    }
  }

  @Override
  public void setCryptoSuite() {

    try {
      CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
      hfcaClient.setCryptoSuite(cryptoSuite);
    } catch (CryptoException | org.hyperledger.fabric.sdk.exception.InvalidArgumentException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
      LOGGER.error("{}",e);
    }
  }

  @Override
  public Enrollment enroll(String user, String secret, EnrollmentRequest enrollmentRequest)
      throws EnrollmentException, InvalidArgumentException {

    Enrollment enrollment = hfcaClient.enroll(user, secret, enrollmentRequest);

    return enrollment;
  }

  @Override
  public Enrollment register(RegistrationRequest registrationRequest, Admin admin, String user) {
    try {
      String enrollmentSecret = hfcaClient.register(registrationRequest, admin);
      return hfcaClient.enroll(user, enrollmentSecret);
    } catch (RegistrationException | InvalidArgumentException | EnrollmentException e) {
      LOGGER.error("{}",e);
    }
    return null;
  }

}
