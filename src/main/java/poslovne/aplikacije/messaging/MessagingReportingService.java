package poslovne.aplikacije.messaging;

import org.springframework.stereotype.Component;

@Component
public class MessagingReportingService {
    public void receiveMessage(ProizvodEvent event) {
        System.out.println("New event: <" + event.getEventType() + 
            "> on proizvod with id = " + event.getProizvod().getId());
    }
}