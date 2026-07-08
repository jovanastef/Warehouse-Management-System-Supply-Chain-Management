package poslovne.aplikacije.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poslovne.aplikacije.billing.BillingService;
import poslovne.aplikacije.inventory.InventoryMessageListener;
import poslovne.aplikacije.orders.OrdersMessageListener;
import poslovne.aplikacije.orders.ShipmentRequest;

@Component
public class MessagingReportingService {
    
    @Autowired
    private OrdersMessageListener ordersMessageListener;
    
    @Autowired
    private InventoryMessageListener inventoryMessageListener;
    
    @Autowired
    private BillingService billingService;
    
    public void receiveMessage(Object message) {
        // Ako je byte[] konvertuj u string za debug
        if (message instanceof byte[]) {
            String json = new String((byte[]) message);
            System.out.println("Received raw message: " + json);
            return;
        }
        
        System.out.println("Primljena poruka tipa: " + message.getClass().getSimpleName());
        
        // Obradi razlicite tipove poruka
        if (message instanceof ProizvodEvent) {
            ProizvodEvent event = (ProizvodEvent) message;
            System.out.println("New event: <" + event.getEventType() + 
                "> on proizvod with id = " + event.getProizvod().getId());
        } 
        else if (message instanceof ShipmentRequest) {
            //  SAMO LOGUJ
            ShipmentRequest request = (ShipmentRequest) message;
            System.out.println("Primljen ShipmentRequest za porudzbinu: " + request.getPorudzbinaId());
            System.out.println("Status: " + request.getStatus() + 
                ", Rok za uplatu: " + request.getRokZaUplatu());
            // BillingScheduler ce automatski proveriti istekle rokove
        }
        else if (message instanceof PaymentConfirmedEvent) {
            ordersMessageListener.receiveMessage(message);
        } 
        else if (message instanceof GoodsSoldEvent || 
                 message instanceof CancelReservationEvent) {
            inventoryMessageListener.receiveMessage(message);
        } 
        else {
            System.out.println("Received message: " + message.toString());
        }
    }
}