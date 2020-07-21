package com.example.fabcarapplication.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ActivityDTO implements Serializable {

  String id;
  long date;
  String type;
  long amount;
  String purpose;
  int percentage;
  //  String agreedUsers;
//  String disAgreedUsers;
  long createAt;
  String description;
  String actorId;
  List<String> proofImage;
  long  lastCreateAt;
  List<String> actorApprovedId;

  public void setListActorApprovedId(String actorApprovedId) {

    this.actorApprovedId = new ArrayList<>();

    String[] listActorApprovedId = actorApprovedId.split(",");

    for (String actor:listActorApprovedId
    ) {
      this.actorApprovedId.add(actor);
    }

  }
}
