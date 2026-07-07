package poslovne.aplikacije.orders;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialLoadOrders {
    
    private static final Logger log = LoggerFactory.getLogger(InitialLoadOrders.class);
    
    @Bean
    public CommandLineRunner initOrders(OrdersService ordersService) {
        return (args) -> {
            // Kreiraj test porudzbinu
            Porudzbina porudzbina = new Porudzbina(
                "POR-TEST-001",
                "KUPAC001",
                "Marko Markovic",
                "Kneza Milosa 15",
                "Beograd"
            );
            
            // Dodaj stavke
            StavkaPorudzbine stavka1 = new StavkaPorudzbine(
                1L, // Koka Kola
                "Koka Kola",
                10.0,
                40.0, // nabavna cena
                46.0  // prodajna cena (15% marza)
            );
            
            StavkaPorudzbine stavka2 = new StavkaPorudzbine(
                2L, // Hleb
                "Hleb beli",
                5.0,
                35.0, // nabavna cena
                40.25 // prodajna cena (15% marza)
            );
            
            porudzbina.dodajStavku(stavka1);
            porudzbina.dodajStavku(stavka2);
            
            Porudzbina savedPorudzbina = ordersService.createOrder(porudzbina);
            log.info("Kreirana test porudzbina: " + savedPorudzbina.toString());
        };
    }
}