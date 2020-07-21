package com.example.fabcarapplication.caching;

import com.example.fabcarapplication.dto.ActivityDTO;
import com.example.fabcarapplication.dto.StatementDTO;
import com.example.fabcarapplication.model.Activity;
import com.example.fabcarapplication.model.Statement;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CachingService {

  public static final String ACTIVITIES_KEY = "activities";

  public static final String STATEMENTS_KEY = "statements";

  public static final String STATEMENTS_TIME_KEY = "statements:time";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CachingService.class);
  private static final String DELIM = ":";

  private final RedisTemplate redisTemplate;

  public static boolean needToUpdateActivities = true;

  public static boolean needtoUpdateStatements = true;

  public static boolean needtoUpdateStatementsByTime = true;

  public CachingService(
      RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public boolean checkKey(String key) {
    return redisTemplate.hasKey(key);
  }

  public void delKey(String key) {
    redisTemplate.delete(key);
  }

  public void updateCachingActivities(JsonArray arrayActivities, String key) {

    Gson gson = new Gson();

    Iterator iActivities = arrayActivities.iterator();

    while (iActivities.hasNext()) {

      JsonElement jsonElement = (JsonElement) iActivities.next();

      Activity activity = getActivity(gson, jsonElement);

      ActivityDTO activityDTO = getActivityDTOFromModel(activity);

      redisTemplate.opsForZSet().add(key, activityDTO, activityDTO.getDate());

    }

    needToUpdateActivities = false;

  }

  public <T> List<T> getPage(int pageSize, int page, String KEY) {
    if (pageSize <= 0 || page <= 0) {
      LOGGER.info("invalid page size: " + pageSize);
    }

    int fromIndex = (page - 1) * pageSize;

    if (redisTemplate.hasKey(KEY) != null) {

//      Set<Object> objectsSet = redisTemplate.opsForZSet()
//          .reverseRange(KEY, fromIndex,
//              Math.min(fromIndex + pageSize, getTotalObjects(STATEMENTS_KEY)) - 1);

      Set<Object> objectsSet = redisTemplate.opsForZSet()
          .reverseRange(KEY, fromIndex, fromIndex + pageSize - 1);

      int size = objectsSet.size();
      List<Object> objectsList = new ArrayList<>(size);
      objectsList.addAll(objectsSet);

      return (List<T>) objectsList;
    } else {
      return Collections.emptyList();
    }
  }

  public <T> List<T> getPage(int pageSize, int page, String KEY, long start, long end) {
    if (pageSize <= 0 || page <= 0) {
      LOGGER.info("invalid page size: " + pageSize);
    }

    int fromIndex = (page - 1) * pageSize;

    if (redisTemplate.hasKey(KEY) != null) {

//      Set<Object> objectsSet = redisTemplate.opsForZSet()
//          .reverseRange(KEY, fromIndex,fromIndex + pageSize - 1);

      Set<Object> objectsSet = redisTemplate.opsForZSet()
          .reverseRangeByScore(KEY, start, end, fromIndex, fromIndex + pageSize - 1);

      int size = objectsSet.size();
      List<Object> objectsList = new ArrayList<>(size);
      objectsList.addAll(objectsSet);

      return (List<T>) objectsList;
    } else {
      return Collections.emptyList();
    }
  }

  public static Activity getActivity(Gson gson, JsonElement jsonElement) {
    Activity activity;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    activity = gson.fromJson(record, Activity.class);

    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    activity.setId(id);

    return activity;
  }

  public static ActivityDTO getActivityDTOFromModel(Activity activity) {
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setId(activity.getId().split(":")[1]);
    activityDTO.setDate(Long.parseLong(activity.getDate()));
    activityDTO.setType(activity.getType());
    activityDTO.setAmount(Long.parseLong(activity.getAmount()));
    activityDTO.setPurpose(activity.getPurpose());
    activityDTO.setPercentage(Integer.parseInt(activity.getPercentage()));
    activityDTO.setActorId(activity.getActorId());
    activityDTO.setCreateAt(Long.parseLong(activity.getCreateAt()));
    activityDTO.setDescription(activity.getDescription());
    activityDTO.setListActorApprovedId(activity.getActorApprovedId());
//    activityDTO.setActorApprovedId(activity.getActorApprovedId());
//    activityDTO.setProofImage(activity.getProofImage());
    activityDTO.setLastCreateAt(Long.parseLong(activity.getLastCreateAt()));
    return activityDTO;
  }

  public int getTotalObjects(String KEY) {

    long sizeOfObjects;

    if (redisTemplate.hasKey(KEY) != null) {
      sizeOfObjects = redisTemplate.opsForZSet().zCard(KEY);
      return Math.toIntExact(sizeOfObjects);
    }
    return 0;
  }

  public void updateCachingStatements(JsonArray arrayStatements) {

    redisTemplate.delete(STATEMENTS_KEY);

    Gson gson = new Gson();

    Iterator iStatements = arrayStatements.iterator();

    while (iStatements.hasNext()) {

      JsonElement jsonElement = (JsonElement) iStatements.next();

//      Activity activity = getActivity(gson, jsonElement);

      Statement statement = getStatement(gson, jsonElement);

//      ActivityDTO activityDTO = getActivityDTOFromModel(activity);

      StatementDTO statementDTO = getStatementDTOFromModel(statement);

      redisTemplate.opsForZSet().add(STATEMENTS_KEY, statementDTO,
          Double.parseDouble(statementDTO.getId()));

    }

    needtoUpdateStatements = false;

  }


  private String generateStatementKey(int year, int quarter, String id) {
    return year + DELIM + quarter + DELIM + id;
  }

  private StatementDTO getStatementDTOFromModel(Statement statement) {

    StatementDTO statementDTO = new StatementDTO();
    statementDTO.setId(statement.getKey().split(":")[1]);
    statementDTO.setTitle(statement.getTitle());
    statementDTO.setDescription(statement.getDescription());
    statementDTO.setQuarter(Integer.parseInt(statement.getQuarter()));
    statementDTO.setYear(Integer.parseInt(statement.getYear()));
//    statementDTO.setProof(statement.getProof());
    return statementDTO;

  }

  private Statement getStatement(Gson gson, JsonElement jsonElement) {
    Statement statement;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    statement = gson.fromJson(record, Statement.class);

    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    statement.setKey(id);

    return statement;
  }

  public void updateCachingStatementsByTime(JsonArray arrayStatements) {

    redisTemplate.delete(STATEMENTS_TIME_KEY);

    Gson gson = new Gson();

    Iterator iStatements = arrayStatements.iterator();

    while (iStatements.hasNext()) {

      JsonElement jsonElement = (JsonElement) iStatements.next();

//      Activity activity = getActivity(gson, jsonElement);

      Statement statement = getStatement(gson, jsonElement);

//      ActivityDTO activityDTO = getActivityDTOFromModel(activity);

      StatementDTO statementDTO = getStatementDTOFromModel(statement);

      redisTemplate.opsForZSet().add(STATEMENTS_TIME_KEY, statementDTO,
          Double.parseDouble(statementDTO.getId()));

    }

    needtoUpdateStatementsByTime = false;

  }

  public int getTotalObjectsInByDate(String KEY, long start, long end) {
    long sizeOfObjects;

    if (redisTemplate.hasKey(KEY) != null) {
//      sizeOfObjects = redisTemplate.opsForZSet().zCard(KEY);
      sizeOfObjects = redisTemplate.opsForZSet().count(KEY, start, end);
      return Math.toIntExact(sizeOfObjects);
    }
    return 0;
  }
}
