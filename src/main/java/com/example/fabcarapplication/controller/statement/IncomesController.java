package com.example.fabcarapplication.controller.statement;

import com.example.fabcarapplication.dto.chart.IncomesChartDTO;
import com.example.fabcarapplication.dto.statement.IncomesStatementDTO;
import com.example.fabcarapplication.service.IncomesStatementService;
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
@RequestMapping(value = "api/fabfund/incomesstatements")
public class IncomesController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(IncomesController.class);

  @Autowired
  IncomesStatementService incomesStatementService;

  @GetMapping(path = "", produces = "application/json")
  public @ResponseBody
  String getStatements() {
    return new String(incomesStatementService.queryAllStatements());
  }

  @GetMapping(path = "/charts", produces = "application/json")
  public @ResponseBody
  List<IncomesChartDTO> getIncomesChart(@RequestParam(defaultValue = "0") String start,
      @RequestParam(defaultValue = "inf") String end,
      @RequestParam(defaultValue = "") String quarter,
      @RequestParam(defaultValue = "") String year){
    return incomesStatementService.getIncomesChart(quarter, year, start, end);
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public @ResponseBody
  String getUniqueStatement(@PathVariable String id) {
    String key = "istatement:" + id;
    return new String(incomesStatementService.getStatementById(key));
  }

  @Secured("ROLE_ADMIN")
  @PostMapping(path = "", consumes = "application/json", produces = "application/json")
  public @ResponseBody
  boolean addStatement(@RequestBody IncomesStatementDTO incomesStatementDTO) {

    try {
      incomesStatementService.addStatement(incomesStatementDTO);
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
      @RequestBody IncomesStatementDTO incomesStatementDTO) {
    try {
      incomesStatementService.updateStatement(id, incomesStatementDTO);
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
      incomesStatementService.deleteStatement(id);
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
    return new String(incomesStatementService.getHistoryStatement(id));
  }

}
