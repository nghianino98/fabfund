package com.example.fabcarapplication.dto.overview;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OverviewResponse {

  int numberOfShareholder;
  long initialCapital;
  long totalAsset;
  long totalRevenue;

}
