package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.chart.FinancialChartDTO;
import com.example.fabcarapplication.dto.statement.FinancialStatementDTO;
import com.example.fabcarapplication.model.statement.FinancialStatement;
import java.util.ArrayList;

public interface FinancialStatementService {

  byte[] queryAllStatements();

  byte[] getStatementById(String key);

  FinancialStatement getFinOverview(String key);

  void addStatement(FinancialStatementDTO financialStatementDTO);

  void updateStatement(String id, FinancialStatementDTO financialStatementDTO);

  void deleteStatement(String id);

  byte[] getHistoryStatement(String id);

  ArrayList<FinancialChartDTO> getFinancialChart(String quarter, String year, String start,
      String end);
}
