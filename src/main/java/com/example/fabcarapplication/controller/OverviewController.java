package com.example.fabcarapplication.controller;

import com.example.fabcarapplication.dto.overview.OverviewResponse;
import com.example.fabcarapplication.service.OverviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/fabfund/overview")
@Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON"})
public class OverviewController {

  @Autowired
  OverviewService overviewService;

  @Autowired

  private static final Logger LOGGER = LoggerFactory
      .getLogger(OverviewController.class);


  @GetMapping(path = "", produces = "application/json")
  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON"})
  public @ResponseBody
  OverviewResponse getOverview() {
    return overviewService.getOverview();
  }
}
