package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.caching.CachingService;
import com.example.fabcarapplication.config.connection.GatewayBuilderConfig;
import com.example.fabcarapplication.config.member.UserConfig;
import com.example.fabcarapplication.dto.StatementDTO;
import com.example.fabcarapplication.payload.response.PaginationResult;
import com.example.fabcarapplication.model.Statement;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.example.fabcarapplication.service.StatementService;
import com.example.fabcarapplication.service.WalletService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
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
public class StatementServiceImpl implements StatementService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(StatementServiceImpl.class);

  @Autowired
  ContractService contractService;

  @Autowired
  CachingService cachingService;

  @Override
  public byte[] queryByteStatements() {
    // Contract myContract = initConn();

    Contract myContract = contractService.getMyContract();

    try {
      byte[] result = myContract.evaluateTransaction("queryAllStatements");
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public PaginationResult getStatementsWithPagination(int limit, int page) {
    if (CachingService.needtoUpdateStatements) {
      String stringStatements = new String(queryByteStatements());

      JsonArray arrayStatements = (JsonArray) JsonParser.parseString(stringStatements);

      cachingService.updateCachingStatements(arrayStatements);
    }

    List<Object> resultPagination = cachingService.getPage(limit, page, CachingService.STATEMENTS_KEY);

    int totalPages = getTotalPages(limit, CachingService.STATEMENTS_KEY);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;
  }

  @Override
  public byte[] queryByteStatementsByTime(String quarter, String year) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryAllStatementsByTime",quarter, year);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public PaginationResult getStatementsWithPaginationByTime(int limit, int page, String quarter, String year) {
//    if (CachingService.needtoUpdateStatementsByTime) {
      String stringStatements = new String(queryByteStatementsByTime(quarter, year));

      JsonArray arrayStatements = (JsonArray) JsonParser.parseString(stringStatements);

      cachingService.updateCachingStatementsByTime(arrayStatements);
//    }

    List<Object> resultPagination = cachingService.getPage(limit, page, CachingService.STATEMENTS_TIME_KEY);

    int totalPages = getTotalPages(limit, CachingService.STATEMENTS_TIME_KEY);

    PaginationResult result = new PaginationResult(totalPages, resultPagination);

    return result;
  }

  private int getTotalPages(int limit, String key) {
    int total = (cachingService.getTotalObjects(key) / limit);
    if (cachingService.getTotalObjects(key)%limit != 0){
      total++;
    }
    return total;
  }

  @Override
  public byte[] getStatementById(String key) {
//    String key = "fstatement:" + id;
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    try {
      byte[] result = myContract.evaluateTransaction("queryStatement", key);
      // closeConnection();
      return result;
    } catch (ContractException e) {
      LOGGER.error("{}",e);
    }
    // closeConnection();
    return new byte[0];
  }

  @Override
  public void addStatement(StatementDTO statementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    Statement statement = getStatementFromDTO(statementDTO);

    if (isExist(statement.getKey())) {
      LOGGER.info("Statement is already exists");
    } else {
      try {
        LOGGER.info("Statement isn't exist, creating..");
        myContract.submitTransaction("createStatement",
            statement.getKey(),
            statement.getTitle(),
            statement.getDescription(),
            statement.getQuarter(),
            statement.getYear(),
            statement.getProof());

        CachingService.needtoUpdateStatements = true;
        CachingService.needtoUpdateStatementsByTime = true;
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

  private Statement getStatementFromDTO(StatementDTO statementDTO) {

    Statement statement = new Statement();
    statement.setKey("statement:" + statementDTO.getId());
    statement.setTitle(statementDTO.getTitle());
    statement.setDescription(statementDTO.getDescription());
    statement.setQuarter(String.valueOf(statementDTO.getQuarter()));
    statement.setYear(String.valueOf(statementDTO.getYear()));
    statement.setProof(statementDTO.getProof());
    return statement;

  }

  @Override
  public void updateStatement(String id, StatementDTO statementDTO) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    Statement statement = getStatementFromDTO(statementDTO);

    if (isExist(statement.getKey())) {
      LOGGER.info("Statement is already exists, updating..");
      try {
        myContract.submitTransaction("updateStatement",
//            statement.getKey(),
            "statement:"+id,
            statement.getTitle(),
            statement.getDescription(),
            statement.getQuarter(),
            statement.getYear(),
            statement.getProof());

        CachingService.needtoUpdateStatements = true;
        CachingService.needtoUpdateStatementsByTime = true;
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        LOGGER.info(e.getMessage());
      }
    } else {
      LOGGER.info("Statement isn't exists");
      // closeConnection();
    }

  }

  @Override
  public void deleteStatement(String id) {

    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "statement:" + id;

    if (isExist(key)) {
      LOGGER.info("Statement is already exists, deleting..");
      try {
        myContract.submitTransaction("deleteStatement", key);
        CachingService.needtoUpdateStatements = true;
        CachingService.needtoUpdateStatementsByTime = true;
        // closeConnection();
      } catch (ContractException | TimeoutException | InterruptedException e) {
        // closeConnection();
        LOGGER.info(e.getMessage());
      }
    } else {
      // closeConnection();
      LOGGER.info("Statement isn't exists");
    }

  }

  @Override
  public byte[] getHistoryStatement(String id) {
    // Contract myContract = initConn();
    Contract myContract = contractService.getMyContract();
    String key = "statement:" + id;

    if (isExist(key)) {
      LOGGER.info("Statement is already exists, getting history..");

      try {
        byte[] result = myContract.evaluateTransaction("queryHistory", key);
        // closeConnection();
        return result;
      } catch (ContractException e) {
        LOGGER.error("{}",e);
      }
    } else {
      LOGGER.info("Statement isn't exists");
    }
    // closeConnection();
    return new byte[0];
  }


}
