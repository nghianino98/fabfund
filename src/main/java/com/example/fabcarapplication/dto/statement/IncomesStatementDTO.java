package com.example.fabcarapplication.dto.statement;

import lombok.Data;

@Data
public class IncomesStatementDTO {

  int id;
  long TotalRevenue;
  long CoreRevenue;
  long FinancialRevenue;
  long OtherRevenue;
  float ProfitBeforeTax;
  float ProfitAfterTax;
  String ProofImage;
  long CreateAt;
  long LastCreateAt;
  int quarter;
  int year;
}
