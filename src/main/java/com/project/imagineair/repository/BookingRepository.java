package com.project.imagineair.repository;

import com.project.imagineair.model.Booking;
import com.project.imagineair.model.PendingBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
  Booking findByBookingCode(String bookingCode);
}
