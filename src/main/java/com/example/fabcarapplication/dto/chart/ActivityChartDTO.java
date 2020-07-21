package com.example.fabcarapplication.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityChartDTO {

  String label;
  long amount;
  int year;
  int month;

}
