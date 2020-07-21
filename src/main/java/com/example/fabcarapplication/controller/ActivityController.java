package com.example.fabcarapplication.controller;

import com.example.fabcarapplication.caching.CachingService;
import com.example.fabcarapplication.dto.ActivityDTO;
import com.example.fabcarapplication.dto.SummaryResponse;
import com.example.fabcarapplication.dto.chart.ActivityChartDTO;
import com.example.fabcarapplication.model.Activity;
import com.example.fabcarapplication.payload.response.PaginationResult;
import com.example.fabcarapplication.service.ActivityService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "api/fabfund/activities")
@Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
public class ActivityController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ActivityController.class);

  private static int addNew = 0;

  @Autowired
  ActivityService activityService;

  @GetMapping(path = "", produces = "application/json")
  public @ResponseBody
  PaginationResult getActivities(@RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "-1") long start,
      @RequestParam(defaultValue = "-1") long end,
      @RequestParam(defaultValue = "-1") String type) {

    if(addNew==0) {
      ActivityDTO newDTO = new ActivityDTO();
      newDTO.setId("1000");
      newDTO.setActorId("a");
      newDTO.setAmount(1L);
      newDTO.setCreateAt(11L);
      newDTO.setDate(11L);
      newDTO.setType("123");
      newDTO.setPurpose("pru");
      newDTO.setPercentage(1);
      newDTO.setDescription("disc");
      newDTO.setLastCreateAt(11L);
//      newDTO.setListActorApprovedId("a,b");
      newDTO.setProofImage(Collections.singletonList("proof"));

      addActivity(newDTO);
      deleteActivity("1000");
      addNew = 1;
    }

    if (-1 == start || -1 == end) {
      if ("-1".equals(type)) {
        return activityService.getActivitiesWithPagination(limit, page);
      } else {
        return activityService.getActivitiesWithPaginationByType(limit, page, type);
      }
    } else {
      if ("-1".equals(type)) {
        return activityService.getActivitiesByDateWithPagination(limit, page, start, end);
      }
      else {
        return activityService.getActivitiesByDateWithPaginationByType(limit, page, start, end, type);
      }
    }
  }

  @GetMapping(path = "/actorId", produces = "application/json")
  public @ResponseBody
  SummaryResponse getActivities(@RequestParam(defaultValue = "0") String start,
      @RequestParam(defaultValue = "inf") String end,
      @RequestParam(defaultValue = "") String type,
      @RequestParam(defaultValue = "") String actorId,
      @RequestParam(defaultValue = "") String actorApprovedId) {
    return activityService.getActivitiesByActorIdAndActorApprovedId(start, end, type, actorId, actorApprovedId);
  }

  @GetMapping(path = "/chart", produces = "application/json")
  public @ResponseBody
  List<ActivityChartDTO> getChart(@RequestParam(defaultValue = "1577811600000") String start,
      @RequestParam(defaultValue = "1609347600000") String end,
      @RequestParam(defaultValue = "") String type,
      @RequestParam(defaultValue = "") String actorId){
    return activityService.getActivitiesChart(start, end, type, actorId);
  }

  @GetMapping(path = "/history/{id}", produces = "application/json")
  public @ResponseBody
  String getActivities(@PathVariable String id) {
    return new String(activityService.getHistoryActivity(id));
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  ActivityDTO getUniqueActivity(@PathVariable String id) {
    String key = "activity:" + id;

    byte[] bt = activityService.getActivityById(key);

    String strResult = new String(bt);

    JsonElement jsonElement = JsonParser.parseString(strResult);

    Gson gson = new Gson();

    Activity activity;
    activity = gson.fromJson(jsonElement, Activity.class);
    activity.setId(key);
//    Activity activity = CachingService.getActivity(gson, jsonElement);
    ActivityDTO activityDTO = CachingService.getActivityDTOFromModel(activity);

    List<String> arrayProofImageSplit = Arrays.asList(activity.getProofImage().split(","));
    List<String> arrayProofImage = new ArrayList<>();
    for (int i = 0; i < arrayProofImageSplit.size() ; i++) {

      if (i%2==0){
        String proofImage = arrayProofImageSplit.get(i) + "," +arrayProofImageSplit.get(i+1);
        arrayProofImage.add(proofImage);
      }

    }


    activityDTO.setProofImage(arrayProofImage);

    return activityDTO;

//    return new String(activityService.getActivityById(key));
  }


  @Secured("ROLE_ADMIN")
  @PostMapping(path = "", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean addActivity(@RequestBody ActivityDTO ActivityDTO) {
//    System.out.println(ActivityDTO);
    try {
      activityService.addActivity(ActivityDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return activityService.getActivitiesWithPagination(10,1);

  }

  @Secured("ROLE_ADMIN")
  @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean updateActivity(@PathVariable String id,
      @RequestBody ActivityDTO ActivityDTO) {

    try {
      activityService.updateActivity(id, ActivityDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return activityService.getActivitiesWithPagination(10,1);

  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  boolean deleteActivity(@PathVariable String id) {

    try {
      activityService.deleteActivity(id);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return activityService.getActivitiesWithPagination(10,1);

  }

}
