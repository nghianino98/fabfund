package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.dto.Type2ActivityDTO;
import com.example.fabcarapplication.dto.Type3ActivityDTO;
import com.example.fabcarapplication.model.TypeActivity;
import com.example.fabcarapplication.service.TypeService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl implements TypeService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TypeServiceImpl.class);

  @Autowired
  ContractService contractService;

  @Override
  public List<Type2ActivityDTO> getAllType2() {
    Contract myContract = contractService.getMyContract();
    List<Type2ActivityDTO> resultList = new ArrayList<>();
    try {
      byte[] result = myContract.evaluateTransaction("queryAllTypeLv2");

      String stringFinancial = new String(result);

      JsonArray arrayFinancial = (JsonArray) JsonParser.parseString(stringFinancial);

      Iterator iType = arrayFinancial.iterator();

      Gson gson = new Gson();

      while (iType.hasNext()) {

        JsonElement jsonElement = (JsonElement) iType.next();

        TypeActivity typeActivity = getTypeActivity(gson, jsonElement);

        Type2ActivityDTO type2ActivityDTO = getType2ActivityDTOFromModel(typeActivity);

        resultList.add(type2ActivityDTO);

      }

      return resultList;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();

    return null;
  }

  @Override
  public List<Type3ActivityDTO> getType2ByType3(String type2Id) {
    Contract myContract = contractService.getMyContract();
    List<Type3ActivityDTO> resultList = new ArrayList<>();
    try {
      byte[] result = myContract.evaluateTransaction("queryAllTypeLv3ByLv3",
          type2Id);

      String stringFinancial = new String(result);

      JsonArray arrayFinancial = (JsonArray) JsonParser.parseString(stringFinancial);

      Iterator iType = arrayFinancial.iterator();

      Gson gson = new Gson();

      while (iType.hasNext()) {

        JsonElement jsonElement = (JsonElement) iType.next();

        TypeActivity typeActivity = getTypeActivity(gson, jsonElement);

        Type3ActivityDTO type3ActivityDTO = getType3ActivityDTOFromModel(typeActivity);

        resultList.add(type3ActivityDTO);

      }

      return resultList;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();

    return null;
  }

  private Type3ActivityDTO getType3ActivityDTOFromModel(TypeActivity typeActivity) {

    Type3ActivityDTO result = new Type3ActivityDTO();
    result.setIdLv2(Integer.parseInt(typeActivity.getType2()));
    result.setIdLv3(Integer.parseInt(typeActivity.getType3()));
    result.setLabel(typeActivity.getLabel3());

    return result;

  }

  @Override
  public boolean addType2(Type2ActivityDTO type2ActivityDTO) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    TypeActivity typeActivity = getType2ActivityFromDTO(type2ActivityDTO);

//    if (isExist(statement.getKey())) {
//      LOGGER.info("Statement is already exists");
//    }
//    else {
    try {
      LOGGER.info("Type creating..");
      myContract
          .submitTransaction("createTypeLv2", typeActivity.getType2(), typeActivity.getLabel2());

      // closeConnection();
    } catch (ContractException | TimeoutException | InterruptedException e) {
      LOGGER.error("{}",e);
      return false;
    }
    // closeConnection();
//    }

    return true;
  }

  private TypeActivity getType2ActivityFromDTO(Type2ActivityDTO type2ActivityDTO) {

    TypeActivity typeActivity = new TypeActivity();

    typeActivity.setType2(String.valueOf(type2ActivityDTO.getId()));
    typeActivity.setLabel2(type2ActivityDTO.getLabel());
    typeActivity.setType3("");
    typeActivity.setLabel3(type2ActivityDTO.getDescription());
    return typeActivity;
  }

  @Override
  public boolean addType3(Type3ActivityDTO type3ActivityDTO) {
    Contract myContract = contractService.getMyContract();
    TypeActivity typeActivity = getType3ActivityFromDTO(type3ActivityDTO);
    try {
      LOGGER.info("Type 3 creating..");
      myContract
          .submitTransaction("createTypeLv3", typeActivity.getType2(), typeActivity.getType3(), typeActivity.getLabel3());

      // closeConnection();
    } catch (ContractException | TimeoutException | InterruptedException e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
  }

  private TypeActivity getType3ActivityFromDTO(Type3ActivityDTO type3ActivityDTO) {

    TypeActivity typeActivity = new TypeActivity();

    typeActivity.setType2(String.valueOf(type3ActivityDTO.getIdLv2()));
    typeActivity.setType3(String.valueOf(type3ActivityDTO.getIdLv3()));
    typeActivity.setLabel3(type3ActivityDTO.getLabel());

    return typeActivity;
  }

  private Type2ActivityDTO getType2ActivityDTOFromModel(TypeActivity typeActivity) {

    Type2ActivityDTO result = new Type2ActivityDTO();
    result.setId(Integer.parseInt(typeActivity.getType2()));
    result.setLabel(typeActivity.getLabel2());
    result.setDescription(typeActivity.getLabel3());

    return result;
  }

  private TypeActivity getTypeActivity(Gson gson, JsonElement jsonElement) {

    TypeActivity typeActivity;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    typeActivity = gson.fromJson(record, TypeActivity.class);

//    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    return typeActivity;
  }
}
