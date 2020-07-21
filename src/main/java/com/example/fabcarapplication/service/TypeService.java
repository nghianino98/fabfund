package com.example.fabcarapplication.service;

import com.example.fabcarapplication.dto.Type2ActivityDTO;
import com.example.fabcarapplication.dto.Type3ActivityDTO;
import java.util.List;

public interface TypeService {

  List<Type2ActivityDTO> getAllType2();

  List<Type3ActivityDTO> getType2ByType3(String type2Id);

  boolean addType2(Type2ActivityDTO type2ActivityDTO);

  boolean addType3(Type3ActivityDTO type3ActivityDTO);

}
