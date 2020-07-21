package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.StatementDTO;
import com.example.fabcarapplication.payload.response.PaginationResult;

public interface StatementService {

  byte[] queryByteStatements();

  PaginationResult getStatementsWithPagination(int limit, int page);

  byte[] getStatementById(String key);

  void addStatement(StatementDTO statementDTO);

  void updateStatement(String id, StatementDTO statementDTO);

  void deleteStatement(String id);

  byte[] getHistoryStatement(String id);

  byte[] queryByteStatementsByTime(String quarter, String year);

  PaginationResult getStatementsWithPaginationByTime(int limit, int page, String quarter, String year);
}
