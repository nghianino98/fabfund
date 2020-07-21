package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.ActivityDTO;
import com.example.fabcarapplication.dto.SummaryResponse;
import com.example.fabcarapplication.dto.chart.ActivityChartDTO;
import com.example.fabcarapplication.payload.response.PaginationResult;
import java.util.List;
import java.util.Map;

public interface ActivityService {

  byte[] queryByteActivities();

  byte[] queryByteActivitiesByDate(String start, String end, String type);

  byte[] queryByteActivitiesByType(String type);

  byte[] queryByteActivitiesByActorIdByActorApprovedId(String start, String end, String type,
      String actorId, String actorApprovedId);

  PaginationResult getActivitiesWithPagination(int limit, int page);

//  SummaryResponse getActivitiesByActorIdByActorApprovedId(String actorId, String approvedActorId);

  byte[] getActivityById(String id);

  void addActivity(ActivityDTO activityDTO);

  void updateActivity(String id, ActivityDTO activityDTO);

  void deleteActivity(String id);

  byte[] getHistoryActivity(String id);

  PaginationResult getActivitiesByDateWithPagination(int limit, int page, long start, long end);

  PaginationResult getActivitiesWithPaginationByType(int limit, int page, String type);

  PaginationResult getActivitiesByDateWithPaginationByType(int limit, int page, long start,
      long end, String type);

  SummaryResponse getActivitiesByActorIdAndActorApprovedId(String start, String end,
      String type, String actorId, String actorApprovedId);

  List<ActivityChartDTO> getActivitiesChart(String start, String end, String type, String actorId);
}
