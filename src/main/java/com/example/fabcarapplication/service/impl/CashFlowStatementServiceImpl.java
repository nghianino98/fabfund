package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.config.connection.GatewayBuilderConfig;
import com.example.fabcarapplication.config.member.UserConfig;
import com.example.fabcarapplication.dto.chart.CashFlowChartDTO;
import com.example.fabcarapplication.dto.statement.CashFlowStatementDTO;
import com.example.fabcarapplication.model.statement.CashFlowStatement;
import com.example.fabcarapplication.service.CashFlowStatementService;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.example.fabcarapplication.service.WalletService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashFlowStatementServiceImpl implements CashFlowStatementService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CashFlowStatementServiceImpl.class);
  
  @Autowired
  ContractService contractService;

  @Override
  public byte[] queryAllStatements() {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryAllCashFlowStatements");
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
      byte[] result = myContract.evaluateTransaction("queryCashFlowStatement", key);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public void addStatement(CashFlowStatementDTO cashFlowStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    CashFlowStatement cashFlowStatement = getCashFlowStatementFromDTO(cashFlowStatementDTO);

    if (isExist(cashFlowStatement.getKey())) {
      LOGGER.info("Cash Flow Statement is already exists");
    } else {
      try {
        LOGGER.info("Cash Flow Statement isn't exist, creating..");
        myContract.submitTransaction("createCashFlowStatement",
            cashFlowStatement.getKey(),
            cashFlowStatement.getOutputActivityBusiness(),
            cashFlowStatement.getInputActivityBusiness(),
            cashFlowStatement.getOutputActivityInvestment(),
            cashFlowStatement.getInputActivityInvestment(),
            cashFlowStatement.getOutputActivityFinancial(),
            cashFlowStatement.getInputActivityFinancial(),
            cashFlowStatement.getProofImage(),
            cashFlowStatement.getCreateAt(),
            cashFlowStatement.getLastCreateAt(),
            cashFlowStatement.getQuarter(),
            cashFlowStatement.getYear());
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

  private CashFlowStatement getCashFlowStatementFromDTO(CashFlowStatementDTO cashFlowStatementDTO) {

    CashFlowStatement cashFlowStatement = new CashFlowStatement();

    cashFlowStatement.setKey("csstatement:" + cashFlowStatementDTO.getId());
    cashFlowStatement.setOutputActivityBusiness(
        String.valueOf(cashFlowStatementDTO.getOutputActivityBusiness()));
    cashFlowStatement.setInputActivityBusiness(
        String.valueOf(cashFlowStatementDTO.getInputActivityBusiness()));
    cashFlowStatement.setOutputActivityInvestment(
        String.valueOf(cashFlowStatementDTO.getOutputActivityInvestment()));
    cashFlowStatement.setInputActivityInvestment(
        String.valueOf(cashFlowStatementDTO.getInputActivityInvestment()));
    cashFlowStatement.setOutputActivityFinancial(
        String.valueOf(cashFlowStatementDTO.getOutputActivityFinancial()));
    cashFlowStatement.setInputActivityFinancial(
        String.valueOf(cashFlowStatementDTO.getInputActivityFinancial()));
    cashFlowStatement.setCreateAt(String.valueOf(cashFlowStatementDTO.getCreateAt()));
    cashFlowStatement.setLastCreateAt(String.valueOf(cashFlowStatementDTO.getLastCreateAt()));
    cashFlowStatement.setProofImage(cashFlowStatementDTO.getProofImage());
    cashFlowStatement.setQuarter(String.valueOf(cashFlowStatementDTO.getQuarter()));
    cashFlowStatement.setYear(String.valueOf(cashFlowStatementDTO.getYear()));

    return cashFlowStatement;
  }

  @Override
  public void updateStatement(String id, CashFlowStatementDTO cashFlowStatementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    CashFlowStatement cashFlowStatement = getCashFlowStatementFromDTO(cashFlowStatementDTO);

    if (isExist(cashFlowStatement.getKey())) {
      LOGGER.info("Cash Flow Statement is already exists, updating..");
      try {
        myContract.submitTransaction("updateCashFlowStatement",
//            cashFlowStatement.getKey(),
            "csstatement:"+id,
            cashFlowStatement.getOutputActivityBusiness(),
            cashFlowStatement.getInputActivityBusiness(),
            cashFlowStatement.getOutputActivityInvestment(),
            cashFlowStatement.getInputActivityInvestment(),
            cashFlowStatement.getOutputActivityFinancial(),
            cashFlowStatement.getInputActivityFinancial(),
            cashFlowStatement.getProofImage(),
            cashFlowStatement.getCreateAt(),
            cashFlowStatement.getLastCreateAt(),
            cashFlowStatement.getQuarter(),
            cashFlowStatement.getYear());

        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
        // closeConnection();
      }
    } else {
      // closeConnection();
      LOGGER.info("Cash flow Statement isn't exists");
    }
  }

  @Override
  public void deleteStatement(String id) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "csstatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Cash Flow Statement is already exists, deleting..");
      try {
        myContract.submitTransaction("deleteCashFlowStatement", key);
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        // closeConnection();
        LOGGER.info(e.getMessage());
      }
    } else {
      // closeConnection();
      LOGGER.info("Cash Flow Statement isn't exists");
    }

  }

  @Override
  public byte[] getHistoryStatement(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "csstatement:" + id;

    if (isExist(key)) {
      LOGGER.info("Cash Flow Statement is already exists, getting history..");

      try {
        byte[] result = myContract.evaluateTransaction("queryHistory", key);
        // closeConnection();
        return result;
      } catch (ContractException e) {
        LOGGER.error("{}",e);
      }
    } else {
      LOGGER.info("Cash Flow Statement isn't exists");
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public List<CashFlowChartDTO> getCashFlowChart(String quarter,
      String year, String start, String end) {
    Contract myContract = contractService.getMyContract();
    List<CashFlowChartDTO> cashFlowChartDTOS = new ArrayList<>();

    // Contract myContract = initConn();

    try {

      byte[] result = myContract.evaluateTransaction("queryAllCashFlowStatementsByTime",quarter, year, start, end);
      String stringCashFlow = new String(result);
      // closeConnection();

      JsonArray arrayCashFlow = (JsonArray) JsonParser.parseString(stringCashFlow);

      Iterator iCashFlows = arrayCashFlow.iterator();

      Gson gson = new Gson();

      while (iCashFlows.hasNext()) {

        JsonElement jsonElement = (JsonElement) iCashFlows.next();

        CashFlowStatement cashFlowStatement = getCashFlowStatement(gson, jsonElement);

        CashFlowChartDTO cashFlowChartDTO = getCashFlowChartDTOFromModel(cashFlowStatement);

        cashFlowChartDTOS.add(cashFlowChartDTO);
      }

      return cashFlowChartDTOS;

    } catch (ContractException e){
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return null;
  }

  private CashFlowChartDTO getCashFlowChartDTOFromModel(CashFlowStatement cashFlowStatement) {
    return CashFlowChartDTO.builder()
        .id(cashFlowStatement.getKey())
        .createAt(Long.parseLong(cashFlowStatement.getCreateAt()))
        .inputActivityBusiness(Long.parseLong(cashFlowStatement.getInputActivityBusiness()))
        .outputActivityBusiness(Long.parseLong(cashFlowStatement.getOutputActivityBusiness()))
        .inputActivityFinancial(Long.parseLong(cashFlowStatement.getInputActivityFinancial()))
        .outputActivityFinancial(Long.parseLong(cashFlowStatement.getOutputActivityFinancial()))
        .inputActivityInvestment(Long.parseLong(cashFlowStatement.getInputActivityInvestment()))
        .outputActivityInvestment(Long.parseLong(cashFlowStatement.getOutputActivityInvestment()))
        .quarter(Long.parseLong(cashFlowStatement.getQuarter()))
        .build();
  }

  private CashFlowStatement getCashFlowStatement(Gson gson, JsonElement jsonElement) {
    CashFlowStatement cashFlowStatement;

    JsonElement record = jsonElement.getAsJsonObject().get("Record");

    cashFlowStatement = gson.fromJson(record, CashFlowStatement.class);

    String id = String.valueOf(jsonElement.getAsJsonObject().get("Key")).split("\"")[1];

    cashFlowStatement.setKey(id);

    return cashFlowStatement;
  }
}
