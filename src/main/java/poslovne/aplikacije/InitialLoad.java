package poslovne.aplikacije;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poslovne.aplikacije.proizvodi.JedinicaMere;
import poslovne.aplikacije.proizvodi.KategorijeProizvoda;
import poslovne.aplikacije.proizvodi.Proizvod;
import poslovne.aplikacije.proizvodi.Valuta;
import poslovne.aplikacije.repository.ProizvodRepository;
import poslovne.aplikacije.servisi.ProizvodService;

@Configuration
public class InitialLoad {
    private static final Logger log = LoggerFactory.getLogger(InitialLoad.class);
    
    @Bean
    public CommandLineRunner demo(ProizvodRepository repository, ProizvodService proizvodService) {
        return (args) -> {
            Proizvod p = null;
            
            p = new Proizvod(1, "Koka Kola");
            p.setKat(KategorijeProizvoda.BEZALKOHOLNA_PICA);
            p.setJm(JedinicaMere.l); 
            p.setKolicinaMere(0.50);
            p.setJedinicnaCena(50.0);
            p.setValuta(Valuta.RSD);
            proizvodService.noviProizvod(p);
            
            p = new Proizvod(2, "Hleb beli");
            p.setKat(KategorijeProizvoda.NAMIRNICE);
            p.setJm(JedinicaMere.g); 
            p.setKolicinaMere(600);
            p.setJedinicnaCena(45.0);
            p.setValuta(Valuta.RSD);
            proizvodService.noviProizvod(p);
            
            p = new Proizvod(3, "Banane");
            p.setKat(KategorijeProizvoda.VOCE);
            p.setJm(JedinicaMere.kg); 
            p.setKolicinaMere(1);
            p.setJedinicnaCena(120.0);
            p.setValuta(Valuta.RSD);
            proizvodService.noviProizvod(p);
            
            log.info("Proizvodi found with findAll():");
            log.info("-------------------------------");
            for (Proizvod proizvod : repository.findAll()) {
                log.info(proizvod.toString());
            }
        };
    }
}