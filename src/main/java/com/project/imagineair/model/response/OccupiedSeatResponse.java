package com.project.imagineair.model.response;

import com.project.imagineair.model.dto.FlightPriceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupiedSeatResponse extends BaseResponse{
  private List<String> seats;
}
