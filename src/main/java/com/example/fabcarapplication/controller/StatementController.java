package com.example.fabcarapplication.controller;

import com.example.fabcarapplication.dto.StatementDTO;
import com.example.fabcarapplication.payload.response.PaginationResult;
import com.example.fabcarapplication.service.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping(value = "api/fabfund/statements")
@Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
public class StatementController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(StatementController.class);

  @Autowired
  StatementService statementService;

  @GetMapping(path = "", produces = "application/json")
  public @ResponseBody
  PaginationResult getStatements(@RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "-1") int quarter,
      @RequestParam(defaultValue = "-1") int year) {
    if (-1 == year && -1 == quarter) {
      return statementService.getStatementsWithPagination(limit, page);
    } else if (-1 == quarter) {
      return statementService
          .getStatementsWithPaginationByTime(limit, page, "",
              String.valueOf(year));
    } else if (-1 == year) {
      return statementService
          .getStatementsWithPaginationByTime(limit, page, String.valueOf(quarter),
              "");
    } else {
      return statementService
          .getStatementsWithPaginationByTime(limit, page, String.valueOf(quarter),
              String.valueOf(year));
    }
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  String getUniqueStatement(@PathVariable String id) {
    String key = "statement:" + id;
    return new String(statementService.getStatementById(key));
  }

  @Secured("ROLE_ADMIN")
  @PostMapping(path = "", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean addStatement(@RequestBody StatementDTO statementDTO) {

    try {
      statementService.addStatement(statementDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
  }

  @Secured("ROLE_ADMIN")
  @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean updateStatement(@PathVariable String id,
      @RequestBody StatementDTO statementDTO) {
    try {
      statementService.updateStatement(id, statementDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;

  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  boolean deleteStatement(@PathVariable String id) {

    try {
      statementService.deleteStatement(id);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
  }

  @GetMapping(path = "/history/{id}", produces = "application/json")
  public @ResponseBody
  String getActivities(@PathVariable String id) {
    return new String(statementService.getHistoryStatement(id));
  }

}
