package com.example.fabcarapplication.dto.statement;

import lombok.Data;

@Data
public class FinancialStatementDTO {

  String id;
  String AuditorOption;
  long TotalDebt;
  long ShortTermDebt;
  long LongTermDebt;
  long InitialCapital;
  long TotalAsset;
  String ProofImage;
  long CreateAt;
  long LastCreateAt;
  int quarter;
  int year;
}


