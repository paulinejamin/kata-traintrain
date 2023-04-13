import com.traintrain.WebTicketManager;
import com.traintrain.booking.TrainBookingService;
import com.traintrain.topology.TrainTopologyService;
import org.junit.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class WebTicketManagerTest {

    @Test
    public void should_check_that_train_has_no_more_than_x_percent_booked_seats() throws Exception {
        // GIVEN
        TrainTopologyService trainTopologyService = new TrainTopologyServiceStub();
        TrainBookingService trainBookingService = new TrainBookingServiceStub("bookRef");
        WebTicketManager webTicketManager = new WebTicketManager(trainTopologyService, trainBookingService);
        // WHEN
        String result = webTicketManager.reserve("train123", 5);
        // THEN
        then(result).contains("{{\"train_id\": \"train123\"");
//        then(result).contains("{{\"train_id\": \"train123\", \"booking_reference\": \"%s\", \"numberOfSeatsToBook\": %s}}")

    }

    static class TrainTopologyServiceStub implements TrainTopologyService {

        @Override
        public String getTrainTopology(String trainId) {
            if (trainId.equals("train123")) return TrainTopologyFactory.withTenAvailableSeats();
            return "";
        }
    }
    static class TrainBookingServiceStub implements TrainBookingService {

        private final String bookingReference;

        public TrainBookingServiceStub(String bookingReference) {
            this.bookingReference = bookingReference;
        }

        @Override
        public String startBooking() {
            return bookingReference;
        }
    }
}
