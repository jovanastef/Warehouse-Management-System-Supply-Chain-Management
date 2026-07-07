package poslovne.aplikacije.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poslovne.aplikacije.proizvodi.Proizvod;
import poslovne.aplikacije.repository.ProizvodRepository;

// Za testiranje
@Configuration
public class InitialLoadInventory {
    
    private static final Logger log = LoggerFactory.getLogger(InitialLoadInventory.class);
    
    @Bean
    public CommandLineRunner initInventory(MagacinRepository magacinRepository, 
                                           StanjeZalihaRepository stanjeZalihaRepository,
                                           ProizvodRepository proizvodRepository) {
        return (args) -> {
            // Kreiraj magacine
            Magacin m1 = new Magacin("M001", "Centralni Magacin Beograd", "Bulevar Oslobodjenja 123", "Beograd");
            Magacin m2 = new Magacin("M002", "Magacin Novi Sad", "Njegoseva 45", "Novi Sad");
            Magacin m3 = new Magacin("M003", "Magacin Nis", "Kralja Petra 78", "Nis");
            
            magacinRepository.save(m1);
            magacinRepository.save(m2);
            magacinRepository.save(m3);
            
            log.info("Magacini kreirani:");
            magacinRepository.findAll().forEach(m -> log.info(m.toString()));
            
            // Dodaj zalihe za proizvode
            Proizvod p1 = proizvodRepository.findById(1L).orElse(null); // Koka Kola
            Proizvod p2 = proizvodRepository.findById(2L).orElse(null); // Hleb
            Proizvod p3 = proizvodRepository.findById(3L).orElse(null); // Banane
            
            if (p1 != null) {
                stanjeZalihaRepository.save(new StanjeZaliha(m1, p1, 100.0));
                stanjeZalihaRepository.save(new StanjeZaliha(m2, p1, 50.0));
            }
            
            if (p2 != null) {
                stanjeZalihaRepository.save(new StanjeZaliha(m1, p2, 200.0));
                stanjeZalihaRepository.save(new StanjeZaliha(m3, p2, 150.0));
            }
            
            if (p3 != null) {
                stanjeZalihaRepository.save(new StanjeZaliha(m2, p3, 75.0));
                stanjeZalihaRepository.save(new StanjeZaliha(m3, p3, 60.0));
            }
            
            log.info("Zalihe kreirane:");
            stanjeZalihaRepository.findAll().forEach(z -> log.info(z.toString()));
        };
    }
}