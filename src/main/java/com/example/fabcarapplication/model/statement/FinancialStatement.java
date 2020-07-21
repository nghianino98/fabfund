package com.example.fabcarapplication.model.statement;

import lombok.Builder;
import lombok.Data;

@Data
public class FinancialStatement {

  String id;
  String auditorOption  ;
  String totalDebt      ;
  String shortTermDebt  ;
  String longTermDebt   ;
  String initialCapital ;
  String totalAsset     ;
  String proofImage     ;
  String createAt    ;
  String lastCreateAt ;
  String quarter;
  String year;
  
}


