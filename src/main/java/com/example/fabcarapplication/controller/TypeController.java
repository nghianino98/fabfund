package com.example.fabcarapplication.controller;

import com.example.fabcarapplication.dto.StatementDTO;
import com.example.fabcarapplication.dto.Type2ActivityDTO;
import com.example.fabcarapplication.dto.Type3ActivityDTO;
import com.example.fabcarapplication.service.TypeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/fabfund/types")
@Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
public class TypeController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TypeController.class);

  @Autowired
  TypeService typeService;

  @GetMapping(path = "/level2", produces = "application/json")
  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
  public @ResponseBody
  List<Type2ActivityDTO> getType2() {
    return typeService.getAllType2();
  }

  @GetMapping(path = "/level3", produces = "application/json")
  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
  public @ResponseBody
  List<Type3ActivityDTO> getType3(@RequestParam(defaultValue = "") String type2) {
    return typeService.getType2ByType3(type2);
  }

  @PostMapping(path = "/level2", consumes = "application/json", produces = "application/json")
  @Secured("ROLE_ADMIN")
  public @ResponseBody
  boolean addType(@RequestBody Type2ActivityDTO type2ActivityDTO) {

    try {
      typeService.addType2(type2ActivityDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
  }

  @PostMapping(path = "/level3", consumes = "application/json", produces = "application/json")
  @Secured("ROLE_ADMIN")
  public @ResponseBody
  boolean addType(@RequestBody Type3ActivityDTO type3ActivityDTO) {

    try {
      typeService.addType3(type3ActivityDTO);
    } catch (Exception e) {
      LOGGER.error("{}",e);
      return false;
    }
    return true;
  }


}
