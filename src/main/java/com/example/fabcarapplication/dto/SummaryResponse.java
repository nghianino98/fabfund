package com.example.fabcarapplication.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SummaryResponse {

  private long sumInput;
  private long anotherActorSumInput;
  private long numberOfInputActivities;

  private long sumOutput;
  private long anotherActorSumOutput;
  private long numberOfOutputActivities;

  private List<ActivityDTO> activities;

}
