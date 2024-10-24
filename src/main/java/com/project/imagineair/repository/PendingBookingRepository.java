package com.project.imagineair.repository;

import com.project.imagineair.model.PendingBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingBookingRepository extends MongoRepository<PendingBooking, String> {
  void deleteByBookingCode(String bookingCode);
  PendingBooking findByBookingCode(String bookingCode);
}
