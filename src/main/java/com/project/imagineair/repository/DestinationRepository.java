package com.project.imagineair.repository;

import com.project.imagineair.model.Destination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends MongoRepository<Destination, String> {

}
