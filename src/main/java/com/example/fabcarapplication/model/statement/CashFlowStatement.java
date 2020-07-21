package com.example.fabcarapplication.model.statement;

import lombok.Builder;
import lombok.Data;

@Data

public class CashFlowStatement {

  String key;
  String outputActivityBusiness;
  String inputActivityBusiness;
  String outputActivityInvestment;
  String inputActivityInvestment;
  String outputActivityFinancial;
  String inputActivityFinancial;
  String proofImage;
  String createAt;
  String lastCreateAt;
  String quarter;
  String year;

}
