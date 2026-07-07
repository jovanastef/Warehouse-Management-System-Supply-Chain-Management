package poslovne.aplikacije.orders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.messaging.GoodsSoldEvent;
import poslovne.aplikacije.messaging.PaymentConfirmedEvent;

@Component
public class OrdersMessageListener {

    @Autowired
    private PorudzbinaRepository porudzbinaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void receiveMessage(Object message) {
        if (message instanceof PaymentConfirmedEvent) {
            handlePaymentConfirmed((PaymentConfirmedEvent) message);
        }
    }

    private void handlePaymentConfirmed(PaymentConfirmedEvent event) {
        System.out.println("Primljen PaymentConfirmed za porudžbinu: " + event.getPorudzbinaId());
        
        Porudzbina porudzbina = porudzbinaRepository.findById(event.getPorudzbinaId())
            .orElseThrow(() -> new RuntimeException("Porudžbina nije pronađena"));

        // Kreiraj fakturu (ovde samo simulacija, u realnosti bi bio entitet Faktura)
        System.out.println("Kreirana faktura za porudžbinu: " + porudzbina.getSifraPorudzbine() 
            + " Iznos: " + event.getIznos());

        // Azuriraj status
        porudzbina.setStatus(StatusPorudzbine.ISPORUCENA);
        porudzbinaRepository.save(porudzbina);

        // Posalji GoodsSold event Inventory modulu
        List<Long> proizvodIds = new ArrayList<>();
        List<Double> kolicine = new ArrayList<>();
        
        for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
            proizvodIds.add(stavka.getProizvodId());
            kolicine.add(stavka.getTrazenaKolicina());
        }

        GoodsSoldEvent goodsSoldEvent = new GoodsSoldEvent(
            porudzbina.getId(), proizvodIds, kolicine, 1L // Pretpostavljamo magacin 1
        );

        rabbitTemplate.convertAndSend(
            RabbitMQConfigurator.APP_EVENTS_EXCHANGE,
            "orders.events.goodsSold",
            goodsSoldEvent
        );
        
        System.out.println("Poslat GoodsSoldEvent za porudzbinu: " + porudzbina.getId());
    }
}