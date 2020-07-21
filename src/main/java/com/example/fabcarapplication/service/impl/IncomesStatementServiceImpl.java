package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.dto.chart.IncomesChartDTO;
import com.example.fabcarapplication.dto.statement.IncomesStatementDTO;
import com.example.fabcarapplication.model.statement.FinancialStatement;
import com.example.fabcarapplication.model.statement.IncomesStatement;
import com.example.fabcarapplication.service.IncomesStatementService;
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
public class IncomesStatementServiceImpl implements IncomesStatementService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(IncomesStatementServiceImpl.class);

  @Autowired
  ContractService contractService;

  @Override
  public byte[] queryAllStatements() {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract.evaluateTransaction("queryAllIncomesStatements");
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
      byte[] result = myContract.evaluateTransaction("queryIncomesStatement", key);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public void addStatement(IncomesStatementDTO incomesStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    IncomesStatement incomesStatement = getIncomesStatementFromDTO(incomesStatementDTO);

    if (isExist(incomesStatement.getKey())) {
      LOGGER.info("Incomes Statement is already exists");
    } else {
      try {
        LOGGER.info("Incomes Statement isn't exist, creating..");
        myContract.submitTransaction("createIncomesStatement",
            incomesStatement.getKey(),
            incomesStatement.getTotalRevenue(),
            incomesStatement.getCoreRevenue(),
            incomesStatement.getFinancialRevenue(),
            incomesStatement.getOtherRevenue(),
            incomesStatement.getProfitBeforeTax(),
            incomesStatement.getProfitAfterTax(),
            incomesStatement.getProofImage(),
            incomesStatement.getCreateAt(),
            incomesStatement.getLastCreateAt(),
            incomesStatement.getQuarter(),
            incomesStatement.getYear());
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.error("{}",e);
      }
      // closeConnection();
    }

  }

  private boolean isExist(String id) {
    boolean checkExist = false;

    if (getStatementById(id).length != 0) {
      checkExist = true;
    }
    return checkExist;
  }

  private IncomesStatement getIncomesStatementFromDTO(IncomesStatementDTO incomesStatementDTO) {

    IncomesStatement incomesStatement = new IncomesStatement();

    incomesStatement.setKey("istatement:" + incomesStatementDTO.getId());
    incomesStatement.setTotalRevenue(String.valueOf(incomesStatementDTO.getTotalRevenue()));
    incomesStatement.setCoreRevenue(String.valueOf(incomesStatementDTO.getCoreRevenue()));
    incomesStatement.setFinancialRevenue(String.valueOf(incomesStatementDTO.getFinancialRevenue()));
    incomesStatement.setOtherRevenue(String.valueOf(incomesStatementDTO.getOtherRevenue()));
    incomesStatement.setProfitBeforeTax(String.valueOf(incomesStatementDTO.getProfitBeforeTax()));
    incomesStatement.setProfitAfterTax(String.valueOf(incomesStatementDTO.getProfitAfterTax()));
    incomesStatement.setProofImage(incomesStatementDTO.getProofImage());
    incomesStatement.setCreateAt(String.valueOf(incomesStatementDTO.getCreateAt()));
    incomesStatement.setLastCreateAt(String.valueOf(incomesStatementDTO.getLastCreateAt()));
    incomesStatement.setQuarter(String.valueOf(incomesStatementDTO.getQuarter()));
    incomesStatement.setYear(String.valueOf(incomesStatementDTO.getYear()));
    return incomesStatement;
  }

  @Override
  public void updateStatement(String id, IncomesStatementDTO incomesStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    IncomesStatement incomesStatement = getIncomesStatementFromDTO(incomesStatementDTO);

    if (isExist(incomesStatement.getKey())) {
      LOGGER.info("Incomes Statement is already exists, updating..");
      try {
        myContract.submitTransaction("updateIncomesStatement",
//            incomesStatement.getKey(),
            "istatement:" + id,
            incomesStatement.getTotalRevenue(),
            incomesStatement.getCoreRevenue(),
            incomesStatement.getFinancialRevenue(),
            incomesStatement.getOtherRevenue(),
            incomesStatement.getProfitBeforeTax(),
            incomesStatement.getProfitAfterTax(),
            incomesStatement.getProofImage(),
            incomesStatement.getCreateAt(),
            incomesStatement.getLastCreateAt(),
            incomesStatement.getQuarter(),
            incomesStatement.getYear());

        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
        // closeConnection();
      }
    } else {
      // closeConnection();
      LOGGER.info("Incomes Statement isn't exists");
    }

  }

  @Override
  public void deleteStatement(String id) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "istatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Incomes Statement is already exists, deleting..");
      try {
        myContract.submitTransaction("deleteIncomesStatement", key);
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        // closeConnection();
        LOGGER.info(e.getMessage());
      }
    } else {
      // closeConnection();
      LOGGER.info("Incomes Statement isn't exists");
    }

  }

  @Override
  public byte[] getHistoryStatement(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "istatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Incomes Statement is already exists, getting history..");

      try {
        byte[] result = myContract.evaluateTransaction("queryHistory", key);
        // closeConnection();
        return result;
      } catch (ContractException e) {
        LOGGER.error("{}",e);
      }
    } else {
      LOGGER.info("Incomes Statement isn't exists");
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public List<IncomesChartDTO> getIncomesChart(String quarter, String year, String start,
      String end) {
    Contract myContract = contractService.getMyContract();
    List<IncomesChartDTO> incomesChartDTOS = new ArrayList<>();

    // Contract myContract = initConn();

    try {

      byte[] result = myContract
          .evaluateTransaction("queryAllIncomesStatementsByTime", quarter, year, start, end);
      String stringIncomes = new String(result);
      // closeConnection();

      JsonArray arrayIncomes = (JsonArray) JsonParser.parseString(stringIncomes);

      Iterator iIncomes = arrayIncomes.iterator();

      Gson gson = new Gson();

      while (iIncomes.hasNext()) {

        JsonElement jsonElement = (JsonElement) iIncomes.next();

        IncomesStatement incomesStatement = getIncomesStatement(gson, jsonElement);

        IncomesChartDTO incomesChartDTO = getIncomesChartDTOFromModel(incomesStatement);

        incomesChartDTOS.add(incomesChartDTO);
      }

      return incomesChartDTOS;

    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return null;
  }

  @Override
  public IncomesStatement getIncOverview(String key) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryIncomesOverview", key);
      // closeConnection();

      String stringFinancial = new String(result);

      JsonArray arrayFinancial = (JsonArray) JsonParser.parseString(stringFinancial);

      Iterator iFinancial = arrayFinancial.iterator();

      Gson gson = new Gson();

//      while (iFinancial.hasNext()) {

      JsonElement jsonElement = (JsonElement) iFinancial.next();

      IncomesStatement incomesStatement = getIncomesStatement(gson, jsonElement);

//      FinancialChartDTO financialChartDTO = getFinancialChartDTOFromModel(financialStatement);

//      }

      return incomesStatement;

    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return null;
  }

  private IncomesChartDTO getIncomesChartDTOFromModel(IncomesStatement incomesStatement) {
    return IncomesChartDTO.builder()
        .coreRevenue(Long.parseLong(incomesStatement.getCoreRevenue()))
        .financialRevenue(Long.parseLong(incomesStatement.getFinancialRevenue()))
        .id(incomesStatement.getKey())
        .otherRevenue(Long.parseLong(incomesStatement.getOtherRevenue()))
        .quarter(Integer.parseInt(incomesStatement.getQuarter()))
        .totalRevenue(Long.parseLong(incomesStatement.getTotalRevenue()))
        .build();
  }

  private IncomesStatement getIncomesStatement(Gson gson, JsonElement jsonElement) {
    IncomesStatement incomesStatement;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    incomesStatement = gson.fromJson(record, IncomesStatement.class);

    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    incomesStatement.setKey(id);

    return incomesStatement;
  }
}
