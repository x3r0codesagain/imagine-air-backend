package com.project.imagineair.controller;

import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.response.DestinationResponse;
import com.project.imagineair.rest.response.RestListResponse;
import com.project.imagineair.service.DestinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

  @Autowired
  private DestinationService destinationService;

  @GetMapping("/public-all-access/trending")
  public RestListResponse<DestinationResponse> getTrendingDestination(@RequestParam int max) {
    try {

      List<DestinationResponse> responses = destinationService.getTrendingDestinations(max);

      return new RestListResponse<>(null, null, true, responses);
    } catch (AppExceptionV2 appExceptionV2) {

      log.error("#getTrendingDestination has an error with message: {}", appExceptionV2.getMessage(), appExceptionV2);
      return new RestListResponse<>(appExceptionV2.getMessage(), appExceptionV2.getCode().name(),
          false, null);
    } catch (Exception ex) {

      log.error("#getTrendingDestination has an error with message: {}", ex.getMessage(), ex);
      return new RestListResponse<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
          false, null);
    }
  }

}
