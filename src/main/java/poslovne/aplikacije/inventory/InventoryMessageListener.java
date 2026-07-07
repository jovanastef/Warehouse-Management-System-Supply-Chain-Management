package poslovne.aplikacije.inventory;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poslovne.aplikacije.messaging.CancelReservationEvent;
import poslovne.aplikacije.messaging.GoodsSoldEvent;

@Component
public class InventoryMessageListener {

    @Autowired
    private InventoryService inventoryService;

    public void receiveMessage(Object message) {
        if (message instanceof GoodsSoldEvent) {
            handleGoodsSold((GoodsSoldEvent) message);
        } else if (message instanceof CancelReservationEvent) {
            handleCancelReservation((CancelReservationEvent) message);
        }
    }

    private void handleGoodsSold(GoodsSoldEvent event) {
        System.out.println("Primljen GoodsSoldEvent za porudžbinu: " + event.getPorudzbinaId());
        
        // Kreiraj otpremnicu (DispatchNote) - ovde samo logujemo
        System.out.println("Kreirana otpremnica za magacin: " + event.getMagacinId());

        // Potvrdi rezervaciju (smanji zalihe)
        List<Long> proizvodIds = event.getProizvodIds();
        List<Double> kolicine = event.getKolicine();

        for (int i = 0; i < proizvodIds.size(); i++) {
            inventoryService.potvrdiRezervaciju(
                event.getMagacinId(), 
                proizvodIds.get(i), 
                kolicine.get(i)
            );
        }
        System.out.println("Zalihe azurirane (potvrdjena rezervacija).");
    }

    private void handleCancelReservation(CancelReservationEvent event) {
        System.out.println("Primljen CancelReservationEvent za porudzbinu: " + event.getPorudzbinaId());
        
        // Oslobodi rezervaciju
        List<Long> proizvodIds = event.getProizvodIds();
        List<Double> kolicine = event.getKolicine();

        for (int i = 0; i < proizvodIds.size(); i++) {
            inventoryService.oslobodiRezervaciju(
                1L, // Pretpostavljamo magacin 1
                proizvodIds.get(i), 
                kolicine.get(i)
            );
        }
        System.out.println("Rezervacije oslobodjene.");
    }
}