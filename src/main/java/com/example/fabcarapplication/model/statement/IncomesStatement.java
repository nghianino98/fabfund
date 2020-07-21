package com.example.fabcarapplication.model.statement;

import lombok.Builder;
import lombok.Data;

@Data
public class IncomesStatement {

  String key;
  String totalRevenue;
  String coreRevenue;
  String financialRevenue;
  String otherRevenue;
  String profitBeforeTax;
  String profitAfterTax;
  String proofImage;
  String createAt;
  String lastCreateAt;
  String quarter;
  String year;

}
