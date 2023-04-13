package trainTrain.api;

import com.traintrain.WebTicketManager;
import com.traintrain.booking.TrainBookingServiceWebAdapter;
import com.traintrain.topology.TrainTopologyServiceWebAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;

@RestController
public class ReservationsController {

    @RequestMapping(method = RequestMethod.POST, value = "api/reservations")
    public String update(@RequestBody RequestDto requestDto) throws IOException, InterruptedException {
        Client client = ClientBuilder.newClient();
        WebTicketManager webTicketManager = new WebTicketManager(new TrainTopologyServiceWebAdapter(), new TrainBookingServiceWebAdapter(client));
        return webTicketManager.reserve(requestDto.getTrain_id(), requestDto.getNumber_of_seats());
    }

    @RequestMapping(method = RequestMethod.GET, value = "api/value")
    public String get() {
        return "Reservations";
    }
}
