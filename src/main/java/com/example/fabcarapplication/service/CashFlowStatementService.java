package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.chart.CashFlowChartDTO;
import com.example.fabcarapplication.dto.statement.CashFlowStatementDTO;
import java.util.List;

public interface CashFlowStatementService {

  byte[] queryAllStatements();

  byte[] getStatementById(String key);

  void addStatement(CashFlowStatementDTO cashFlowStatementDTO);

  void updateStatement(String id, CashFlowStatementDTO cashFlowStatementDTO);

  void deleteStatement(String id);

  byte[] getHistoryStatement(String id);

  List<CashFlowChartDTO> getCashFlowChart(String start, String end, String quarter, String year);
}
