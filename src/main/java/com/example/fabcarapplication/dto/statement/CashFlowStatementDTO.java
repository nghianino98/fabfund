package com.example.fabcarapplication.dto.statement;

import lombok.Builder;
import lombok.Data;

@Data
public class CashFlowStatementDTO {

  int id;
  long outputActivityBusiness;
  long inputActivityBusiness;
  long outputActivityInvestment;
  long inputActivityInvestment;
  long outputActivityFinancial;
  long inputActivityFinancial;
  String proofImage;
  long createAt;
  long lastCreateAt;
  int quarter;
  int year;
}
