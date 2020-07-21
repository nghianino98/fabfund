package com.example.fabcarapplication.config.connection;

import java.util.Properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("ca")
@Data
public class CertificateAuthorityConfig {

  public String pemFile;
  public String allowAllHostNames;
  public String url;

  public Properties getProps(){
    Properties props = new Properties();
    props.put("pemFile",
        pemFile);
    props.put("allowAllHostNames", allowAllHostNames);
    return props;
  }





}
