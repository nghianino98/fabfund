package com.example.fabcarapplication.payload.response;

import java.util.List;
import lombok.Data;

@Data
public class PaginationResult {

  public int totalPages;
  public List<Object> records;

  public PaginationResult(int totalPages, List<Object> records) {
    this.totalPages = totalPages;
    this.records = records;
  }
}
