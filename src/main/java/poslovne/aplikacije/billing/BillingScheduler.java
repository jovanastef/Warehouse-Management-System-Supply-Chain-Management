package poslovne.aplikacije.billing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.messaging.CancelReservationEvent;
import poslovne.aplikacije.orders.Porudzbina;
import poslovne.aplikacije.orders.PorudzbinaRepository;
import poslovne.aplikacije.orders.StatusPorudzbine;
import poslovne.aplikacije.orders.StavkaPorudzbine;

@Component
public class BillingScheduler {

    @Autowired
    private PorudzbinaRepository porudzbinaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Pokrece se svakog dana u 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void checkOverduePayments() {
        System.out.println("Provera isteklih rokova za placanje...");
        
        List<Porudzbina> overdueOrders = porudzbinaRepository.findAll().stream()
            .filter(p -> p.getStatus() == StatusPorudzbine.REZERVISANA)
            .filter(p -> p.getRokZaUplatu() != null && p.getRokZaUplatu().isBefore(LocalDate.now()))
            .collect(Collectors.toList());

        for (Porudzbina porudzbina : overdueOrders) {
            System.out.println("Istekao rok za porudzbinu: " + porudzbina.getSifraPorudzbine());
            
            // Pripremi event za otkazivanje rezervacije
            List<Long> proizvodIds = new ArrayList<>();
            List<Double> kolicine = new ArrayList<>();
            
            for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
                proizvodIds.add(stavka.getProizvodId());
                kolicine.add(stavka.getTrazenaKolicina());
            }

            CancelReservationEvent event = new CancelReservationEvent(
                porudzbina.getId(), proizvodIds, kolicine
            );

            rabbitTemplate.convertAndSend(
                RabbitMQConfigurator.APP_EVENTS_EXCHANGE,
                "orders.events.cancelReservation",
                event
            );
            
            // Azuriraj status porudzbine
            porudzbina.setStatus(StatusPorudzbine.OTKAZANA);
            porudzbinaRepository.save(porudzbina);
        }
    }
}