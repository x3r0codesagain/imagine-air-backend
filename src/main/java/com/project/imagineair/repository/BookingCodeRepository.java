package com.project.imagineair.repository;

import com.project.imagineair.model.BookingCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingCodeRepository extends MongoRepository<BookingCode, String> {

}
