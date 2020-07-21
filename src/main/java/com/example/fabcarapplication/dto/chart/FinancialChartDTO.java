package com.example.fabcarapplication.dto.chart;

import java.util.Comparator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialChartDTO implements Comparable<FinancialChartDTO> {

  private String id;
  private long totalDebt;
  private long totalAsset;
  private long createAt;
  private long quarter;
  private long year;


  @Override
  public int compareTo(FinancialChartDTO financialChartDTO) {
    return this.getId().compareTo(financialChartDTO.getId());
  }
}
