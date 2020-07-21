package com.example.fabcarapplication.dto.chart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CashFlowChartDTO {

  private String id;
  private long outputActivityBusiness;
  private long inputActivityBusiness;
  private long outputActivityInvestment;
  private long inputActivityInvestment;
  private long outputActivityFinancial;
  private long inputActivityFinancial;
  private long quarter;
  private long createAt;
}
