package com.example.fabcarapplication.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
public class Activity {

  String id;
  String date;
  String type;
  String amount;
  String purpose;
  String percentage;
  String createAt;
  String description;
  String actorId;
  String proofImage;
  String lastCreateAt;
  String actorApprovedId;
}
