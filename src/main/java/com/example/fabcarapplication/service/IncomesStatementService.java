package com.example.fabcarapplication.service;


import com.example.fabcarapplication.dto.chart.IncomesChartDTO;
import com.example.fabcarapplication.dto.statement.IncomesStatementDTO;
import com.example.fabcarapplication.model.statement.IncomesStatement;
import java.util.List;

public interface IncomesStatementService {

  byte[] queryAllStatements();

  byte[] getStatementById(String key);

  void addStatement(IncomesStatementDTO incomesStatementDTO);

  void updateStatement(String id, IncomesStatementDTO incomesStatementDTO);

  void deleteStatement(String id);

  byte[] getHistoryStatement(String id);

  List<IncomesChartDTO> getIncomesChart(String quarter, String year, String start, String end);

  IncomesStatement getIncOverview(String key);
}
