package com.project.imagineair.repository;

import com.project.imagineair.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {
  List<Flight>findAllByOriginCodeAndArrivalCodeAndDepartDate(String originCode, String arrivalCode, String date);
  Flight findByNumberAndDepartDate(String number, String date);
}
