package com.example.fabcarapplication.controller.statement;

import com.example.fabcarapplication.dto.chart.CashFlowChartDTO;
import com.example.fabcarapplication.dto.statement.CashFlowStatementDTO;
import com.example.fabcarapplication.service.CashFlowStatementService;
import java.util.List;
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

@Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
@RestController
@RequestMapping(value = "api/fabfund/cashflowstatements")
public class CashFlowController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CashFlowController.class);

  @Autowired
  CashFlowStatementService cashFlowStatementService;

  @GetMapping(path = "", produces = "application/json")
  public @ResponseBody
  String getStatements() {
    return new String(cashFlowStatementService.queryAllStatements());
  }

  @GetMapping(path = "/charts", produces = "application/json")
  public @ResponseBody
  List<CashFlowChartDTO> getCashFlowChart(@RequestParam(defaultValue = "0") String start,
      @RequestParam(defaultValue = "inf") String end,
      @RequestParam(defaultValue = "") String quarter,
      @RequestParam(defaultValue = "") String year){
    return cashFlowStatementService.getCashFlowChart(quarter, year, start, end);
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  String getUniqueStatement(@PathVariable String id) {
    String key = "csstatement:" + id;
    return new String(cashFlowStatementService.getStatementById(key));
  }

  @Secured("ROLE_ADMIN")
  @PostMapping(path = "", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean addStatement(@RequestBody CashFlowStatementDTO cashFlowStatementDTO) {

    try {
      cashFlowStatementService.addStatement(cashFlowStatementDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return new String(financialStatementService.queryAllStatements());
  }

  @Secured("ROLE_ADMIN")
  @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean updateStatement(@PathVariable String id,
      @RequestBody CashFlowStatementDTO cashFlowStatementDTO) {
    try {
      cashFlowStatementService.updateStatement(id, cashFlowStatementDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return new String(financialStatementService.queryAllStatements());

  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  boolean deleteStatement(@PathVariable String id) {

    try {
      cashFlowStatementService.deleteStatement(id);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
//    return new String(financialStatementService.queryAllStatements());

  }

  @GetMapping(path = "/history/{id}", produces = "application/json")
  public @ResponseBody
  String getActivities(@PathVariable String id) {
    return new String(cashFlowStatementService.getHistoryStatement(id));
  }

}
