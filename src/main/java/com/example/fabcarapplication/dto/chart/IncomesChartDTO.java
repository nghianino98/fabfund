package com.example.fabcarapplication.dto.chart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncomesChartDTO {

  private String id;
  private long totalRevenue;
  private long coreRevenue;
  private long financialRevenue;
  private long otherRevenue;
  private int quarter;

}
