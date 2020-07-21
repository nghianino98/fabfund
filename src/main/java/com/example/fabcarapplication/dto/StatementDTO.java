package com.example.fabcarapplication.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class StatementDTO implements Serializable {

  String id;
  String title;
  String description;
  int quarter;
  int year;
  String proof;

}
