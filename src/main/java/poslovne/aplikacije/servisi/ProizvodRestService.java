package poslovne.aplikacije.servisi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import poslovne.aplikacije.proizvodi.KategorijeProizvoda;
import poslovne.aplikacije.proizvodi.Proizvod;
import poslovne.aplikacije.repository.ProizvodRepository;

@RestController
public class ProizvodRestService {
    @Autowired
    private ProizvodRepository proizvodRepository;
    
    @Autowired
    private ProizvodService proizvodService;
    
    @GetMapping("/proizvodi")
    List<Proizvod> all() {
        return proizvodRepository.findAll();
    }
    
    @GetMapping("/proizvodi/kategorija/{kat}")
    List<Proizvod> getByKategorija(@PathVariable Integer kat) {
        return proizvodRepository.findByKat(KategorijeProizvoda.of(kat));
    }
    
    @PostMapping("/proizvodi")
    Proizvod noviProizvod(@RequestBody Proizvod noviProizvod) {
        return proizvodService.noviProizvod(noviProizvod);
    }
    
    @PutMapping("/proizvodi/{id}")
    Proizvod azurirajProizvod(@RequestBody Proizvod noviProizvod, @PathVariable Long id) {
        return proizvodService.updateProizvod(noviProizvod, id);
    }
    
    @DeleteMapping("/proizvodi/{id}")
    Proizvod deleteProizvod(@PathVariable Long id) {
        return proizvodService.deleteProizvod(id);
    }
}