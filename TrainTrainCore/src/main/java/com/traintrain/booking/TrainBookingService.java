package com.traintrain.booking;

import com.traintrain.Seat;

import java.util.List;

public interface TrainBookingService {

    String startBooking();
    void finalizeBooking(String trainId, String booking_ref, List<Seat> availableSeats);

}
