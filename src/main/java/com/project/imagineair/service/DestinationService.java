package com.project.imagineair.service;

import com.project.imagineair.model.response.DestinationResponse;

import java.util.List;

public interface DestinationService {
  public List<DestinationResponse> getTrendingDestinations (int max);
}
