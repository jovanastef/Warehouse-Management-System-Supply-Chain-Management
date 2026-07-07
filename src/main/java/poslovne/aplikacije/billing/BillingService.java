package poslovne.aplikacije.billing;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.messaging.PaymentConfirmedEvent;
import poslovne.aplikacije.orders.Porudzbina;
import poslovne.aplikacije.orders.PorudzbinaRepository;
import poslovne.aplikacije.orders.StatusPorudzbine;

@Service
public class BillingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PorudzbinaRepository porudzbinaRepository;

    public void processPayment(Long porudzbinaId) {
        Porudzbina porudzbina = porudzbinaRepository.findById(porudzbinaId)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));

        if (porudzbina.getStatus() != StatusPorudzbine.REZERVISANA) {
            throw new RuntimeException("Porudzbina nije u statusu za placanje");
        }

        // Simulacija uspesne uplate
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(porudzbinaId, porudzbina.getUkupanIznos());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfigurator.APP_EVENTS_EXCHANGE,
            "billing.events.paymentConfirmed",
            event
        );
        
        System.out.println("Poslat PaymentConfirmedEvent za porudzbinu: " + porudzbinaId);
    }
}