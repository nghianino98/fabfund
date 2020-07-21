package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.caching.CachingService;
import com.example.fabcarapplication.dto.ActivityDTO;
import com.example.fabcarapplication.dto.SummaryResponse;
import com.example.fabcarapplication.dto.chart.ActivityChartDTO;
import com.example.fabcarapplication.model.Activity;
import com.example.fabcarapplication.payload.response.PaginationResult;
import com.example.fabcarapplication.service.ActivityService;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {


  private static final Logger LOGGER = LoggerFactory
      .getLogger(ActivityServiceImpl.class);

  @Autowired
  CertificateAuthorityService certificateAuthorityService;

  @Autowired
  ContractService contractService;

  @Autowired
  CachingService cachingService;


  private Activity getActivityFromDTO(
      ActivityDTO activityDTO) {

    Activity activity = new Activity();
    activity.setId("activity:" + activityDTO.getId());
    activity.setDate(String.valueOf(activityDTO.getDate()));
    activity.setType(String.valueOf(activityDTO.getType()));
    activity.setAmount(String.valueOf(activityDTO.getAmount()));
    activity.setPurpose(activityDTO.getPurpose());
    activity.setPercentage(String.valueOf(activityDTO.getPercentage()));
    activity.setActorId(activityDTO.getActorId());
    activity.setCreateAt(String.valueOf(activityDTO.getCreateAt()));
    activity.setDescription(activityDTO.getDescription());
    activity.setActorApprovedId(getStringActorApprovedId(activityDTO.getActorApprovedId()));
//    activity.setProofImage(activityDTO.getProofImage());
    activity.setProofImage(getStringActorApprovedId(activityDTO.getProofImage()));
    activity.setLastCreateAt(String.valueOf(activityDTO.getLastCreateAt()));
//    if (activityDTO.getActorApprovedId() != null) {
//      activity.setActorApprovedId(getStringActorApprovedId(activityDTO.getActorApprovedId()));
//    } else {
//      activity.setActorApprovedId("");
//    }
    return activity;
  }

  private String getStringActorApprovedId(List<String> actorApprovedId) {

    String strActorApprovedId = "";
    for (int i = 0; i < actorApprovedId.size(); i++) {
      strActorApprovedId += actorApprovedId.get(i);
      if (i != actorApprovedId.size() - 1) {
        strActorApprovedId += ",";
      }
    }
    return strActorApprovedId;
  }

  private boolean isExist(String key) {
    boolean checkExist = false;

    if (getActivityById(key).length != 0) {
      checkExist = true;
    }
    return checkExist;
  }

  @Override
  public byte[] queryByteActivities() {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract.evaluateTransaction("queryAllActivities");
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("", e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public byte[] queryByteActivitiesByDate(String start, String end, String type) {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract.evaluateTransaction("queryAllActivitiesByDate", start, end, type);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("", e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public byte[] queryByteActivitiesByType(String type) {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract.evaluateTransaction("queryAllActivitiesByType", type);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("", e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public byte[] queryByteActivitiesByActorIdByActorApprovedId(String start, String end, String type,
      String actorId, String actorApprovedId) {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract
          .evaluateTransaction("queryAllActivitiesByActorIdByApprovedId", actorId, actorApprovedId,
              start, end, type);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("", e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public PaginationResult getActivitiesWithPagination(int limit, int page) {

    // Empty -> need to caching
    if (!cachingService.checkKey(CachingService.ACTIVITIES_KEY)) {

      String stringActivities = new String(queryByteActivities());

      JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);

      cachingService.updateCachingActivities(arrayActivities, CachingService.ACTIVITIES_KEY);

    }

    List<Object> resultPagination = cachingService
        .getPage(limit, page, CachingService.ACTIVITIES_KEY);

    int totalPages = getTotalPages(limit, CachingService.ACTIVITIES_KEY);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;

  }

  @Override
  public PaginationResult getActivitiesByDateWithPagination(int limit, int page, long start,
      long end) {

    // Empty -> need to caching
    if (!cachingService.checkKey(CachingService.ACTIVITIES_KEY)) {

      String stringActivities = new String(queryByteActivities());

      JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);

      cachingService.updateCachingActivities(arrayActivities, CachingService.ACTIVITIES_KEY);

    }

    List<Object> resultPagination = cachingService
        .getPage(limit, page, CachingService.ACTIVITIES_KEY, start, end);

    int totalPages = getTotalPagesByDate(limit, CachingService.ACTIVITIES_KEY, start, end);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;
  }

  @Override
  public PaginationResult getActivitiesWithPaginationByType(int limit, int page, String type) {

    if (!cachingService.checkKey(type)) {

      String stringActivities = new String(queryByteActivitiesByType(type));

      JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);

      cachingService.updateCachingActivities(arrayActivities, type);

    }

    String key = type;

    List<Object> resultPagination = cachingService.getPage(limit, page, key);

    int totalPages = getTotalPages(limit, key);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;

  }

  @Override
  public PaginationResult getActivitiesByDateWithPaginationByType(int limit, int page, long start,
      long end, String type) {
    if (!cachingService.checkKey(type)) {

      String stringActivities = new String(queryByteActivitiesByType(type));

      JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);

      cachingService.updateCachingActivities(arrayActivities, type);

    }

    String key = type;

    List<Object> resultPagination = cachingService.getPage(limit, page, key, start, end);

    int totalPages = getTotalPagesByDate(limit, key, start, end);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;
  }

  @Override
  public SummaryResponse getActivitiesByActorIdAndActorApprovedId(String start, String end,
      String type, String actorId, String actorApprovedId) {

    SummaryResponse result = new SummaryResponse();
    List<ActivityDTO> listActivities = new ArrayList<>();

    String stringActivities = new String(
        queryByteActivitiesByActorIdByActorApprovedId(start, end, type, actorId, actorApprovedId));

    String allStringActivities = new String(
        queryByteActivitiesByActorIdByActorApprovedId(start, end, type, "", actorApprovedId));

//    result.setStringAllActivities(stringActivities);

    JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);
    JsonArray arrayAllActivities = (JsonArray) JsonParser.parseString(allStringActivities);

    long sumInput = 0;
    long anotherActorSumInput = 0;
    long numberOfInputActivities = 0;

    long sumOutput = 0;
    long anotherActorSumOuput = 0;
    long numberOfOutputActivities = 0;

    Gson gson = new Gson();

    Iterator iActivities = arrayActivities.iterator();

    while (iActivities.hasNext()) {
      JsonElement jsonElement = (JsonElement) iActivities.next();
      Activity activity = CachingService.getActivity(gson, jsonElement);
      ActivityDTO activityDTO = CachingService.getActivityDTOFromModel(activity);

      listActivities.add(activityDTO);

      if (activityDTO.getType().contains("input")) {
        sumInput += activityDTO.getAmount();
        numberOfInputActivities++;
      }

      if (activityDTO.getType().contains("output")) {
        sumOutput += activityDTO.getAmount();
        numberOfOutputActivities++;
      }

    }
    result.setSumInput(sumInput);
    result.setNumberOfInputActivities(numberOfInputActivities);
    result.setSumOutput(sumOutput);
    result.setNumberOfOutputActivities(numberOfOutputActivities);
    result.setActivities(listActivities);

    Iterator iAllActivities = arrayAllActivities.iterator();

    while (iAllActivities.hasNext()) {
      JsonElement jsonElement = (JsonElement) iAllActivities.next();
      Activity activity = CachingService.getActivity(gson, jsonElement);
      ActivityDTO activityDTO = CachingService.getActivityDTOFromModel(activity);

      if (activityDTO.getType().contains("input")) {
        anotherActorSumInput += activityDTO.getAmount();

      }

      if (activityDTO.getType().contains("output")) {
        anotherActorSumOuput += activityDTO.getAmount();

      }
    }
    result.setAnotherActorSumInput(anotherActorSumInput);
    result.setAnotherActorSumOutput(anotherActorSumOuput);

    return result;
  }

  @Override
  public List<ActivityChartDTO> getActivitiesChart(String start, String end,
      String type, String actorId) {

    Map<String, ActivityChartDTO> activityChartDTOMap = new HashMap<>();

    Calendar cal = Calendar.getInstance();

    cal.setTimeInMillis(Long.parseLong(start));
    int startYear = cal.get(Calendar.YEAR);
    int startMonth = cal.get(Calendar.MONTH)+1;

    cal.setTimeInMillis(Long.parseLong(end));
    int endYear = cal.get(Calendar.YEAR);
    int endMonth = cal.get(Calendar.MONTH)+1;

    for (int i = startMonth; i<=12; i++){
      activityChartDTOMap.put(i+"/"+startYear,new ActivityChartDTO("", 0, i, startMonth));
    }
    for (int i = startYear+1; i<endYear; i++){
      for (int j = 1; j<=12; j++){
        activityChartDTOMap.put(j+"/"+i,new ActivityChartDTO("", 0, j, i));
      }
    }
    for (int i = 1; i<=endMonth ; i++){
      activityChartDTOMap.put(i+"/"+endYear, new ActivityChartDTO("",0, i, endYear));
    }

//    String stringActivities = new String(queryByteActivitiesByDate(start, end, type));

    String stringActivities = new String(queryByteActivitiesByActorIdByActorApprovedId(start, end, type, actorId, ""));

    JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);

    Gson gson = new Gson();

    Iterator iActivities = arrayActivities.iterator();

    while (iActivities.hasNext()) {
      JsonElement jsonElement = (JsonElement) iActivities.next();
      Activity activity = CachingService.getActivity(gson, jsonElement);
      ActivityChartDTO activityChartDTO = getaCtivityChartDTOFromModel(activity);

      String key = activityChartDTO.getMonth()+"/"+activityChartDTO.getYear();

      ActivityChartDTO updatingActivityChartDTO = activityChartDTOMap.get(key);
      long oldAmount = updatingActivityChartDTO.getAmount();

      if (activity.getType().contains("input:")) {
        updatingActivityChartDTO.setAmount(oldAmount + activityChartDTO.getAmount());
      } else {
        updatingActivityChartDTO.setAmount(oldAmount - activityChartDTO.getAmount());
      }

//      updatingActivityChartDTO.setLabel(key);
      activityChartDTOMap.put(key, updatingActivityChartDTO);

    }

    List<ActivityChartDTO> activityChartDTOList = new ArrayList<>();

    activityChartDTOMap.forEach((s, activityChartDTO) ->
    {
      activityChartDTO.setLabel(s);
      activityChartDTOList.add(activityChartDTO);
    });

    return activityChartDTOList;
  }

  private ActivityChartDTO getaCtivityChartDTOFromModel(Activity activity) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(Long.parseLong(activity.getCreateAt()));

    ActivityChartDTO activityChartDTO = new ActivityChartDTO();

    activityChartDTO.setAmount(Long.parseLong(activity.getAmount()));
    activityChartDTO.setYear(cal.get(Calendar.YEAR));
    activityChartDTO.setMonth(cal.get(Calendar.MONTH));

    return activityChartDTO;

  }

  //  @Override
//  public SummaryResponse getActivitiesByActorIdByActorApprovedId(String actorId,
//      String approvedActorId) {
//
//    String stringActivities = new String(queryByteActivitiesByActorIdByActorApprovedId(actorId, approvedActorId));
//
//    JsonArray arrayActivities = (JsonArray) JsonParser.parseString(stringActivities);
//
//    long summaryType1 = 0;
//
//    Gson gson = new Gson();
//
//    Iterator iActivities = arrayActivities.iterator();
//
//    while (iActivities.hasNext()) {
//
//      JsonElement jsonElement = (JsonElement) iActivities.next();
//
//      Activity activity = CachingService.getActivity(gson, jsonElement);
//
//      ActivityDTO activityDTO = CachingService.getActivityDTOFromModel(activity);
//
//      if (activityDTO.getType()==1){
//        summaryType1 += activityDTO.getAmount();
//      }
//    }
//
//    SummaryResponse summaryResponse = SummaryResponse.builder()
//        .stringActivities(stringActivities)
//        .percentOfCapital(40)
//        .sumByType1(summaryType1)
//        .build();
//
//    return summaryResponse;
//  }


  private int getTotalPagesByDate(int limit, String key, long start, long end) {
    int total = (cachingService.getTotalObjectsInByDate(key, start, end) / limit);
    if (cachingService.getTotalObjectsInByDate(key, start, end) % limit != 0) {
      total++;
    }
    return total;
  }

  private int getTotalPages(int limit, String key) {
    int total = (cachingService.getTotalObjects(key) / limit);
    if (cachingService.getTotalObjects(key) % limit != 0) {
      total++;
    }
    return total;
  }

  @Override
  public byte[] getActivityById(String key) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryActivity", key);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("", e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public void addActivity(ActivityDTO activityDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    Activity activity = getActivityFromDTO(activityDTO);
    activity.setActorApprovedId("VNGCorp, VinGroup");

    if (isExist(activity.getId())) {
      LOGGER.info("Activity is already exists");
    } else {
      try {
        LOGGER.info("Activity isn't exist, creating..");

        myContract.submitTransaction("createActivity",
            activity.getId(),
            activity.getDate(),
            activity.getType(),
            activity.getAmount(),
            activity.getPurpose(),
            activity.getPercentage(),
//            activity.getAgreedUsers(),
//            activity.getDisAgreedUsers(),
            activity.getCreateAt(),
            activity.getDescription(),
            activity.getActorId(),
            activity.getProofImage(),
            activity.getLastCreateAt(),
            activity.getActorApprovedId());
        cachingService.delKey(CachingService.ACTIVITIES_KEY);
//        CachingService.needToUpdateActivities = true;
        // closeConnection();
      } catch (Exception e) {
        LOGGER.error("", e);
      }
    }

  }

  @Override
  public void updateActivity(String id, ActivityDTO activityDTO) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    Activity activity = getActivityFromDTO(activityDTO);

    if (isExist(activity.getId())) {
      LOGGER.info("Activity is already exists, updating..");
      try {
        myContract.submitTransaction("updateActivity",
//            activity.getId(),
            "activity:" + id,
            activity.getDate(),
            activity.getType(),
            activity.getAmount(),
            activity.getPurpose(),
            activity.getPercentage(),
//            activity.getAgreedUsers(),
//            activity.getDisAgreedUsers(),
            activity.getCreateAt(),
            activity.getDescription(),
            activity.getActorId(),
            activity.getProofImage(),
            activity.getLastCreateAt(),
            activity.getActorApprovedId());
        cachingService.delKey(CachingService.ACTIVITIES_KEY);
//        CachingService.needToUpdateActivities = true;
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
      }
    } else {
      LOGGER.info("Activity isn't exists");
      // closeConnection();
    }
  }

  @Override
  public void deleteActivity(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "activity:" + id;

    if (isExist(key)) {
      LOGGER.info("Activity is already exists, deleting..");
      try {
        myContract.submitTransaction("deleteActivity", key);
        cachingService.delKey(CachingService.ACTIVITIES_KEY);
//        CachingService.needToUpdateActivities = true;
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
      }
    } else {
      LOGGER.info("Activity isn't exists");
      // closeConnection();
    }
  }

  @Override
  public byte[] getHistoryActivity(String id) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "activity:" + id;

    if (isExist(key)) {
      LOGGER.info("Activity is already exists, getting history..");

      try {
        byte[] result = myContract.evaluateTransaction("queryHistory", key);
        // closeConnection();
        return result;
      } catch (ContractException e) {
        LOGGER.error("", e);
      }
    } else {
      LOGGER.info("Activity isn't exists");
    }
    // closeConnection();
    return new byte[0];
  }


}
