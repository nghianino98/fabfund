package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.dto.chart.FinancialChartDTO;
import com.example.fabcarapplication.dto.statement.FinancialStatementDTO;
import com.example.fabcarapplication.model.statement.FinancialStatement;
import com.example.fabcarapplication.service.FinancialStatementService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FinancialStatementServiceImpl implements FinancialStatementService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(FinancialStatementServiceImpl.class);

  @Autowired
  ContractService contractService;

  @Override
  public byte[] queryAllStatements() {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryAllFinStatements");
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }


  @Override
  public byte[] getStatementById(String key) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryFinStatement", key);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public FinancialStatement getFinOverview(String key) {
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryFinStatementOverview", key);

      String stringFinancial = new String(result);

      JsonArray arrayFinancial = (JsonArray) JsonParser.parseString(stringFinancial);

      Iterator iFinancial = arrayFinancial.iterator();

      Gson gson = new Gson();

//      while (iFinancial.hasNext()) {

      JsonElement jsonElement = (JsonElement) iFinancial.next();

      FinancialStatement financialStatement = getFinancialStatement(gson, jsonElement);

//      FinancialChartDTO financialChartDTO = getFinancialChartDTOFromModel(financialStatement);

//      }

      return financialStatement;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();

    return null;
  }

  @Override
  public void addStatement(FinancialStatementDTO financialStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    FinancialStatement financialStatement = getFinancialStatementFromDTO(financialStatementDTO);

    if (isExist(financialStatement.getId())) {
      LOGGER.info("Financial Statement is already exists");
    } else {
      try {
        LOGGER.info("Financial Statement isn't exist, creating..");
        myContract.submitTransaction("createFinStatement",
            financialStatement.getId(),
            financialStatement.getAuditorOption(),
            financialStatement.getTotalDebt(),
            financialStatement.getShortTermDebt(),
            financialStatement.getLongTermDebt(),
            financialStatement.getInitialCapital(),
            financialStatement.getTotalAsset(),
            financialStatement.getProofImage(),
            financialStatement.getCreateAt(),
            financialStatement.getLastCreateAt(),
            financialStatement.getQuarter(),
            financialStatement.getYear());
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.error("{}",e);
      }
      // closeConnection();
    }

  }


  @Override
  public void updateStatement(String id, FinancialStatementDTO financialStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    FinancialStatement financialStatement = getFinancialStatementFromDTO(financialStatementDTO);

    if (isExist(financialStatement.getId())) {
      LOGGER.info("Financial Statement is already exists, updating..");
      try {
        myContract.submitTransaction("updateFinStatement",
//            financialStatement.getId(),
            "fstatement:" + id,
            financialStatement.getAuditorOption(),
            financialStatement.getTotalDebt(),
            financialStatement.getShortTermDebt(),
            financialStatement.getLongTermDebt(),
            financialStatement.getInitialCapital(),
            financialStatement.getTotalAsset(),
            financialStatement.getProofImage(),
            financialStatement.getCreateAt(),
            financialStatement.getLastCreateAt(),
            financialStatement.getQuarter(),
            financialStatement.getYear());

        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
        // closeConnection();
      }
    } else {
      // closeConnection();
      LOGGER.info("Financial Statement isn't exists");
    }

  }

  @Override
  public void deleteStatement(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "fstatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Financial Statement is already exists, deleting..");
      try {
        myContract.submitTransaction("deleteFinStatement", key);
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        // closeConnection();
        LOGGER.info(e.getMessage());
      }
    } else {
      // closeConnection();
      LOGGER.info("Financial Statement isn't exists");
    }
  }

  @Override
  public byte[] getHistoryStatement(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "fstatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Financial Statement is already exists, getting history..");

      try {
        byte[] result = myContract.evaluateTransaction("queryHistory", key);
        // closeConnection();
        return result;
      } catch (ContractException e) {
        LOGGER.error("{}",e);
      }
    } else {
      LOGGER.info("Financial Statement isn't exists");
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public ArrayList<FinancialChartDTO> getFinancialChart(String quarter, String year, String start,
      String end) {

    boolean filterAllYear = false;

    if (year.equals("")) {
      filterAllYear = true;
    }

    ArrayList<FinancialChartDTO> financialChartDTOS = new ArrayList<>();
    Contract myContract = contractService.getMyContract();
    // Contract myContract = initConn();

    try {

      byte[] result = myContract
          .evaluateTransaction("queryAllFinStatementsByTime", quarter, year, start, end);
      String stringFinancial = new String(result);
      // closeConnection();

      JsonArray arrayFinancial = (JsonArray) JsonParser.parseString(stringFinancial);

      Iterator iFinancial = arrayFinancial.iterator();

      Gson gson = new Gson();

      while (iFinancial.hasNext()) {

        JsonElement jsonElement = (JsonElement) iFinancial.next();

        FinancialStatement financialStatement = getFinancialStatement(gson, jsonElement);

        FinancialChartDTO financialChartDTO = getFinancialChartDTOFromModel(financialStatement);

        if (filterAllYear == true) {
          if (financialChartDTO.getQuarter() != 5) {
            financialChartDTOS.add(financialChartDTO);
          }
        } else {
          financialChartDTOS.add(financialChartDTO);
        }

      }

      Collections.sort(financialChartDTOS, Collections.reverseOrder());

      return financialChartDTOS;


    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return null;
  }

  private FinancialChartDTO getFinancialChartDTOFromModel(FinancialStatement financialStatement) {
    return FinancialChartDTO.builder()
        .id(financialStatement.getId())
        .createAt(Long.parseLong(financialStatement.getCreateAt()))
        .totalAsset(Long.parseLong(financialStatement.getTotalAsset()))
        .totalDebt(Long.parseLong(financialStatement.getTotalDebt()))
        .quarter(Long.parseLong(financialStatement.getQuarter()))
        .year(Long.parseLong(financialStatement.getYear()))
        .build();
  }

  private FinancialStatement getFinancialStatement(Gson gson, JsonElement jsonElement) {
    FinancialStatement financialStatement;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    financialStatement = gson.fromJson(record, FinancialStatement.class);

    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    financialStatement.setId(id);

    return financialStatement;
  }


  private FinancialStatement getFinancialStatementFromDTO(
      FinancialStatementDTO financialStatementDTO) {

    FinancialStatement financialStatement = new FinancialStatement();
    financialStatement.setId("fstatement:" + financialStatementDTO.getId());
    financialStatement.setAuditorOption(financialStatementDTO.getAuditorOption());
    financialStatement.setInitialCapital(String.valueOf(financialStatementDTO.getInitialCapital()));
    financialStatement.setLongTermDebt(String.valueOf(financialStatementDTO.getLongTermDebt()));
    financialStatement.setShortTermDebt(String.valueOf(financialStatementDTO.getShortTermDebt()));
    financialStatement.setTotalAsset(String.valueOf(financialStatementDTO.getTotalAsset()));
    financialStatement.setTotalDebt(String.valueOf(financialStatementDTO.getTotalDebt()));
    financialStatement.setProofImage(financialStatementDTO.getProofImage());
    financialStatement.setCreateAt(String.valueOf(financialStatementDTO.getCreateAt()));
    financialStatement.setLastCreateAt(String.valueOf(financialStatementDTO.getLastCreateAt()));
    financialStatement.setQuarter(String.valueOf(financialStatementDTO.getQuarter()));
    financialStatement.setYear(String.valueOf(financialStatementDTO.getYear()));
    return financialStatement;
  }

  private boolean isExist(String id) {
    boolean checkExist = false;

    if (getStatementById(id).length != 0) {
      checkExist = true;
    }
    return checkExist;
  }

}
