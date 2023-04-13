package com.traintrain.booking;

import com.traintrain.Seat;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class TrainBookingServiceWebAdapter implements TrainBookingService {

    private static final String uriBookingReferenceService = "http://localhost:51691";
    private final Client client;

    public TrainBookingServiceWebAdapter(Client client) {
        this.client = client;
    }

    @Override
    public String startBooking() {
        WebTarget target = client.target(uriBookingReferenceService + "/booking_reference/");
        return target.request(MediaType.APPLICATION_JSON).get(String.class);
    }

    @Override
    public void finalizeBooking(String trainId, String booking_ref, List<Seat> availableSeats) {
        String postContent = buildPostContent(trainId, bookingRef, availableSeats);

        try {
            WebTarget webTarget = client.target(urITrainDataService + "/reserve/");
            Invocation.Builder request = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
            request.post(Entity.text(postContent));
        } finally {
            client.close();
        }

    }


}
