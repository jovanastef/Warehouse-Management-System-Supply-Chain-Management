package poslovne.aplikacije.orders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.inventory.InventoryService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {
    
    @Autowired
    private PorudzbinaRepository porudzbinaRepository;
    
    @Autowired
    private StavkaPorudzbineRepository stavkaRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final Double DEFAULT_MARZA = 15.0; // 15% marza
    
    // PORUDZBINA OPERACIJE
    
    public Porudzbina createOrder(Porudzbina porudzbina) {
        // Generisi sifru porudzbine ako nije postavljena
        if (porudzbina.getSifraPorudzbine() == null || 
            porudzbina.getSifraPorudzbine().isEmpty()) {
            String sifra = "POR-" + System.currentTimeMillis();
            porudzbina.setSifraPorudzbine(sifra);
        }
        
        // Postavi datum kreiranja
        porudzbina.setDatumKreiranja(LocalDate.now());
        
        porudzbina.setRokZaUplatu(LocalDate.now().plusDays(8));
        
        // Postavi default marzu za svaku stavku (15%)
        for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
            if (stavka.getMarza() == null || stavka.getMarza() == 0) {
                // Izracunaj prodajnu cenu sa default marzom od 15%
                Double nabavnaCena = stavka.getNabavnaCenaPoJedinici();
                if (nabavnaCena != null && nabavnaCena > 0) {
                    Double prodajnaCena = nabavnaCena * (1 + DEFAULT_MARZA / 100);
                    stavka.setProdajnaCenaPoJedinici(prodajnaCena);
                    stavka.setMarza(DEFAULT_MARZA);
                }
            }
        }
        
        Porudzbina savedPorudzbina = porudzbinaRepository.save(porudzbina);
        
        // Posalji event da je kreirana nova porudzbina
        rabbitTemplate.convertAndSend(
            RabbitMQConfigurator.APP_EVENTS_EXCHANGE,
            "orders.events.new",
            new ShipmentRequest(savedPorudzbina)
        );
        
        return savedPorudzbina;
    }
    
    public List<Porudzbina> getAllPorudzbine() {
        return porudzbinaRepository.findAll();
    }
    
    public Optional<Porudzbina> getPorudzbinaById(Long id) {
        return porudzbinaRepository.findById(id);
    }
    
    public Optional<Porudzbina> getPorudzbinaBySifra(String sifra) {
        return porudzbinaRepository.findBySifraPorudzbine(sifra);
    }
    
    public List<Porudzbina> getPorudzbineByKupac(String sifraKupca) {
        return porudzbinaRepository.findBySifraKupca(sifraKupca);
    }
    
    public List<Porudzbina> getPorudzbineByStatus(StatusPorudzbine status) {
        return porudzbinaRepository.findByStatus(status);
    }
    
    // POTVRDA PORUDZBINE I REZERVACIJA
    @Transactional
    public Porudzbina confirmOrder(Long porudzbinaId) {
        Porudzbina porudzbina = porudzbinaRepository.findById(porudzbinaId)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));
        
        // Proveri da li ima dovoljno zaliha i rezervisi robu
        boolean sveRezervisano = true;
        
        for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
            // Za sada pretpostavljamo da rezervisemo iz magacina 1 (centralni)
            // U realnoj implementaciji bi trebalo naci optimalni magacin
            boolean rezervisano = inventoryService.rezervisiRobu(
                1L, // magacinId - treba dinamicki odrediti
                stavka.getProizvodId(),
                stavka.getTrazenaKolicina()
            );
            
            if (!rezervisano) {
                sveRezervisano = false;
                break;
            }
        }
        
        if (sveRezervisano) {
            porudzbina.setStatus(StatusPorudzbine.REZERVISANA);
            porudzbinaRepository.save(porudzbina);
            
            // Posalji ShipmentRequest za dalje procesuiranje
            ShipmentRequest request = new ShipmentRequest(porudzbina);
            rabbitTemplate.convertAndSend(
                RabbitMQConfigurator.APP_EVENTS_EXCHANGE,
                "orders.events.confirmed",
                request
            );
            
            return porudzbina;
        } else {
            // Oslobodi sve rezervacije ako nesto nije moglo da se rezervise
            rollbackRezervacije(porudzbina);
            porudzbina.setStatus(StatusPorudzbine.OTKAZANA);
            porudzbinaRepository.save(porudzbina);
            throw new RuntimeException("Nema dovoljno zaliha za porudzbinu");
        }
    }
    
    private void rollbackRezervacije(Porudzbina porudzbina) {
        for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
            inventoryService.oslobodiRezervaciju(
                1L, // magacinId
                stavka.getProizvodId(),
                stavka.getTrazenaKolicina()
            );
        }
    }
    
    // OTKAZIVANJE PORUDZBINE
    @Transactional
    public Porudzbina cancelOrder(Long porudzbinaId) {
        Porudzbina porudzbina = porudzbinaRepository.findById(porudzbinaId)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));
        
        if (porudzbina.getStatus() == StatusPorudzbine.REZERVISANA) {
            // Oslobodi rezervacije
            for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
                inventoryService.oslobodiRezervaciju(
                    1L,
                    stavka.getProizvodId(),
                    stavka.getTrazenaKolicina()
                );
            }
        }
        
        porudzbina.setStatus(StatusPorudzbine.OTKAZANA);
        return porudzbinaRepository.save(porudzbina);
    }
    
    // ISPORUKA 
    
    public Porudzbina completeOrder(Long porudzbinaId) {
        Porudzbina porudzbina = porudzbinaRepository.findById(porudzbinaId)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));
        
        // Potvrdi rezervacije (smanji zalihe)
        for (StavkaPorudzbine stavka : porudzbina.getStavke()) {
            inventoryService.potvrdiRezervaciju(
                1L,
                stavka.getProizvodId(),
                stavka.getTrazenaKolicina()
            );
        }
        
        porudzbina.setStatus(StatusPorudzbine.ISPORUCENA);
        return porudzbinaRepository.save(porudzbina);
    }
}