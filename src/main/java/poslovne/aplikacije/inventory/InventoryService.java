package poslovne.aplikacije.inventory;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovne.aplikacije.proizvodi.Proizvod;
import poslovne.aplikacije.repository.ProizvodRepository;

@Service
public class InventoryService {
    
    @Autowired
    private MagacinRepository magacinRepository;
    
    @Autowired
    private StanjeZalihaRepository stanjeZalihaRepository;
    
    @Autowired
    private ProizvodRepository proizvodRepository;
    
    // MAGACIN OPERACIJE
    
    public Magacin kreirajMagacin(Magacin magacin) {
        return magacinRepository.save(magacin);
    }
    
    public List<Magacin> getAllMagacini() {
        return magacinRepository.findAll();
    }
    
    public Optional<Magacin> getMagacinById(Long id) {
        return magacinRepository.findById(id);
    }
    
    public Optional<Magacin> getMagacinBySifra(String sifra) {
        return magacinRepository.findBySifraMagacina(sifra);
    }
    
    // ZALIHE OPERACIJE
    
    public StanjeZaliha dodajZalihe(Long magacinId, Long proizvodId, Double kolicina) {
        Magacin magacin = magacinRepository.findById(magacinId)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
        
        Proizvod proizvod = proizvodRepository.findById(proizvodId)
            .orElseThrow(() -> new RuntimeException("Proizvod nije pronadjen"));
        
        Optional<StanjeZaliha> existingZaliha = stanjeZalihaRepository
            .findByMagacinAndProizvod(magacin, proizvod);
        
        if (existingZaliha.isPresent()) {
            StanjeZaliha zaliha = existingZaliha.get();
            zaliha.dodajKolicinu(kolicina);
            return stanjeZalihaRepository.save(zaliha);
        } else {
            StanjeZaliha novaZaliha = new StanjeZaliha(magacin, proizvod, kolicina);
            return stanjeZalihaRepository.save(novaZaliha);
        }
    }
    
    public List<StanjeZaliha> getZaliheZaMagacin(Long magacinId) {
        Magacin magacin = magacinRepository.findById(magacinId)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
        return stanjeZalihaRepository.findByMagacin(magacin);
    }
    
    public List<StanjeZaliha> getZaliheZaProizvod(Long proizvodId) {
        Proizvod proizvod = proizvodRepository.findById(proizvodId)
            .orElseThrow(() -> new RuntimeException("Proizvod nije pronadjen"));
        return stanjeZalihaRepository.findByProizvod(proizvod);
    }
    
    public List<StanjeZaliha> getAllZalihe() {
        return stanjeZalihaRepository.findAll();
    }
    
    // REZERVACIJE
    @Transactional
    public boolean rezervisiRobu(Long magacinId, Long proizvodId, Double kolicina) {
        Magacin magacin = magacinRepository.findById(magacinId)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
        
        Proizvod proizvod = proizvodRepository.findById(proizvodId)
            .orElseThrow(() -> new RuntimeException("Proizvod nije pronadjen"));
        
        Optional<StanjeZaliha> zaliha = stanjeZalihaRepository
            .findByMagacinAndProizvod(magacin, proizvod);
        
        if (zaliha.isPresent()) {
            return zaliha.get().rezervisi(kolicina);
        }
        return false;
    }
    
    public void oslobodiRezervaciju(Long magacinId, Long proizvodId, Double kolicina) {
        Magacin magacin = magacinRepository.findById(magacinId)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
        
        Proizvod proizvod = proizvodRepository.findById(proizvodId)
            .orElseThrow(() -> new RuntimeException("Proizvod nije pronadjen"));
        
        Optional<StanjeZaliha> zaliha = stanjeZalihaRepository
            .findByMagacinAndProizvod(magacin, proizvod);
        
        if (zaliha.isPresent()) {
            zaliha.get().oslobodiRezervaciju(kolicina);
            stanjeZalihaRepository.save(zaliha.get());
        }
    }
    
    @Transactional
    public void potvrdiRezervaciju(Long magacinId, Long proizvodId, Double kolicina) {
        Magacin magacin = magacinRepository.findById(magacinId)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
        
        Proizvod proizvod = proizvodRepository.findById(proizvodId)
            .orElseThrow(() -> new RuntimeException("Proizvod nije pronadjen"));
        
        Optional<StanjeZaliha> zaliha = stanjeZalihaRepository
            .findByMagacinAndProizvod(magacin, proizvod);
        
        if (zaliha.isPresent()) {
            zaliha.get().ukloniRezervisano(kolicina);
            stanjeZalihaRepository.save(zaliha.get());
        }
    }
}