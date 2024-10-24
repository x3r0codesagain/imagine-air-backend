package com.project.imagineair.service.impl;

import com.project.imagineair.model.Destination;
import com.project.imagineair.model.enums.ErrorCodes;
import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.response.DestinationResponse;
import com.project.imagineair.repository.DestinationRepository;
import com.project.imagineair.service.DestinationService;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl implements DestinationService {

  @Autowired
  private DestinationRepository destinationRepository;

  @Autowired
  private Mapper mapper;

  @Override
  public List<DestinationResponse> getTrendingDestinations(int max) {
    List<Destination> destinationList = destinationRepository.findAll();

    if (CollectionUtils.isEmpty(destinationList)) {
      throw new AppExceptionV2(ErrorCodes.DESTINATIONS_UNAVAILABLE.getMessage(),
          ErrorCodes.DESTINATIONS_UNAVAILABLE);
    }

    List<DestinationResponse> responses = destinationList.stream()
        .map(destination -> mapper.map(destination, DestinationResponse.class))
        .sorted(Comparator.comparing(DestinationResponse::getBookingCount))
        .limit(max).collect(Collectors.toList());

    return responses;
  }
}
