package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.dto.chart.FinancialChartDTO;
import com.example.fabcarapplication.dto.overview.OverviewResponse;
import com.example.fabcarapplication.dto.statement.FinancialStatementDTO;
import com.example.fabcarapplication.model.statement.FinancialStatement;
import com.example.fabcarapplication.model.statement.IncomesStatement;
import com.example.fabcarapplication.repository.UserRepository;
import com.example.fabcarapplication.service.AdminService;
import com.example.fabcarapplication.service.FinancialStatementService;
import com.example.fabcarapplication.service.IncomesStatementService;
import com.example.fabcarapplication.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverviewServiceImpl implements OverviewService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AdminService adminService;

  @Autowired
  FinancialStatementService financialStatementService;

  @Autowired
  IncomesStatementService incomesStatementService;

  @Override
  public OverviewResponse getOverview() {
    OverviewResponse response = new OverviewResponse();

    response.setNumberOfShareholder(adminService.getAllShareholderProfiles().size());

    FinancialStatement financialStatement = financialStatementService.getFinOverview("fstatement:14");

    response.setInitialCapital(Long.parseLong(financialStatement.getInitialCapital()));
    response.setTotalAsset(Long.parseLong(financialStatement.getTotalAsset()));

    IncomesStatement incomesStatement = incomesStatementService.getIncOverview("istatement:14");
    response.setTotalRevenue(Long.parseLong(incomesStatement.getTotalRevenue()));

//    String finOverview = new String(financialStatementService.getFinOverview("fstatement:14"));
//
//    String incOverview = new String(incomesStatementService.getIncOverview("istatement:14"));

//    response.setCapitalFinancial();
//
//    response.setSumOfAsset();

//    response.setSumOfRevenue();
    return response;
  }
}
