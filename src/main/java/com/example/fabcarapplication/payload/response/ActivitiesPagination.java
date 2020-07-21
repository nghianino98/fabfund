package com.example.fabcarapplication.payload.response;

import com.example.fabcarapplication.dto.ActivityDTO;
import java.util.List;
import lombok.Data;

@Data
public class ActivitiesPagination {

  int totalPages;
  List<ActivityDTO> activities;

  public ActivitiesPagination(int i, List<ActivityDTO> resultPagination) {

    totalPages = i;
    activities = resultPagination;

  }
}
