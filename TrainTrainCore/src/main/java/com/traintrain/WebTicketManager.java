package com.traintrain;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cache.ITrainCaching;
import com.cache.SeatEntity;
import com.cache.TrainCaching;
import com.traintrain.booking.TrainBookingService;
import com.traintrain.topology.TrainTopologyService;

public class WebTicketManager {
    private static final String urITrainDataService = "http://localhost:50680";
    private ITrainCaching trainCaching;
    private TrainTopologyService trainTopologyService;
    private final TrainBookingService trainBookingService;

    public WebTicketManager(TrainTopologyService trainTopologyService, TrainBookingService trainBookingService) {
        this.trainTopologyService = trainTopologyService;
        this.trainBookingService = trainBookingService;
        trainCaching = new TrainCaching();
        try {
            trainCaching.clear();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String reserve(String trainId, int numberOfSeatsToBook) throws IOException, InterruptedException {
        List<Seat> availableSeats = new ArrayList<Seat>();
        int count = 0;
        String bookingRef;

        // get the trainId
        String jsonTrain = trainTopologyService.getTrainTopology(trainId);
        Train trainInst = new Train(jsonTrain);
        if (checkTrainReservationIsLowEnough(numberOfSeatsToBook, trainInst)) {
            int numberOfReserv = 0;
            // find numberOfSeatsToBook to reserve
            for (int index = 0, i = 0; index < trainInst.seats.size(); index++) {
                Seat each = trainInst.seats.get(index);
                if (each.getBookingRef() == "") {
                    i++;
                    if (i <= numberOfSeatsToBook) {
                        availableSeats.add(each);
                    }
                }
            }

            for (Seat seat : availableSeats) {
                count++;
            }

            int reservedSets = 0;


            if (count != numberOfSeatsToBook) {
                return String.format("{{\"train_id\": \"%s\", \"booking_reference\": \"\", \"numberOfSeatsToBook\": []}}",
                        trainId);
            } else {
                bookingRef = trainBookingService.startBooking();
                for (Seat availableSeat : availableSeats) {
                    availableSeat.setBookingRef(bookingRef);
                    numberOfReserv++;
                    reservedSets++;
                }
            }

            if (numberOfReserv == numberOfSeatsToBook) {

                this.trainCaching.Save(toSeatsEntities(trainId, availableSeats, bookingRef));

                if (reservedSets == 0) {
                    String output = String.format("Reserved seat(s): ", reservedSets);
                    System.out.println(output);
                }

                return String.format(
                        "{{\"train_id\": \"%s\", \"booking_reference\": \"%s\", \"numberOfSeatsToBook\": %s}}",
                        trainId,
                        bookingRef,
                        dumpSeats(availableSeats));
            }

        }
        return String.format("{{\"train_id\": \"%s\", \"booking_reference\": \"\", \"numberOfSeatsToBook\": []}}", trainId);
    }

    private static boolean checkTrainReservationIsLowEnough(int seats, Train trainInst) {
        return (trainInst.reservedSeats + seats) <= Math.floor(ThresholdManager.getMaxRes() * trainInst.getMaxSeat());
    }

    private static String buildPostContent(String trainId, String booking_ref, List<Seat> availableSeats) {
        StringBuilder seats = new StringBuilder("[");

        boolean firstTime = true;

        for (Seat seat : availableSeats) {
            if (!firstTime) {
                seats.append(", ");
            } else {
                firstTime = false;
            }

            seats.append(String.format("\"%d%s\"", seat.getSeatNumber(), seat.getCoachName()));
        }

        seats.append("]");

        String result = String.format(
                "{{\r\n\t\"train_id\": \"%s\",\r\n\t\"seats\": %s,\r\n\t\"booking_reference\": \"%S\"\r\n}}",
                trainId, seats.toString(), booking_ref);

        return result;
    }

    private String dumpSeats(List<Seat> seats) {
        StringBuilder sb = new StringBuilder("[");

        boolean firstTime = true;

        for (Seat seat : seats) {
            if (!firstTime) {
                sb.append(", ");
            } else {
                firstTime = false;
            }

            sb.append(String.format("\"%d%s\"", seat.getSeatNumber(), seat.getCoachName()));
        }

        sb.append("]");

        return sb.toString();
    }

    private List<SeatEntity> toSeatsEntities(String train, List<Seat> availableSeats, String bookingRef) throws InterruptedException {
        List<SeatEntity> seatEntities = new ArrayList<SeatEntity>();
        for (Seat seat : availableSeats) {
            seatEntities.add(new SeatEntity(train, bookingRef, seat.getCoachName(), seat.getSeatNumber()));
        }
        return seatEntities;
    }
}
