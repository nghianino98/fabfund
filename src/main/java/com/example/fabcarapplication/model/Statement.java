package com.example.fabcarapplication.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Statement {

  String key;
  String title;
  String description;
  String quarter;
  String year;
  String proof;

}
