package poslovne.aplikacije.inventory;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    // MAGACIN ENDPOINTS
    
    @GetMapping("/magacini")
    public List<Magacin> getAllMagacini() {
        return inventoryService.getAllMagacini();
    }
    
    @GetMapping("/magacini/{id}")
    public Magacin getMagacinById(@PathVariable Long id) {
        return inventoryService.getMagacinById(id)
            .orElseThrow(() -> new RuntimeException("Magacin nije pronadjen"));
    }
    
    @PostMapping("/magacini")
    public Magacin kreirajMagacin(@RequestBody Magacin magacin) {
        return inventoryService.kreirajMagacin(magacin);
    }
    
    // ZALIHE ENDPOINTS
    
    @GetMapping("/zalihe")
    public List<StanjeZaliha> getAllZalihe() {
        return inventoryService.getAllZalihe();
    }
    
    @GetMapping("/zalihe/magacin/{magacinId}")
    public List<StanjeZaliha> getZaliheZaMagacin(@PathVariable Long magacinId) {
        return inventoryService.getZaliheZaMagacin(magacinId);
    }
    
    @GetMapping("/zalihe/proizvod/{proizvodId}")
    public List<StanjeZaliha> getZaliheZaProizvod(@PathVariable Long proizvodId) {
        return inventoryService.getZaliheZaProizvod(proizvodId);
    }
    
    @PostMapping("/zalihe/dodaj/{magacinId}/{proizvodId}/{kolicina}")
    public StanjeZaliha dodajZalihe(
            @PathVariable Long magacinId,
            @PathVariable Long proizvodId,
            @PathVariable Double kolicina) {
        return inventoryService.dodajZalihe(magacinId, proizvodId, kolicina);
    }
    
    // REZERVACIJE ENDPOINTS
    
    @PostMapping("/rezervisi/{magacinId}/{proizvodId}/{kolicina}")
    public boolean rezervisiRobu(
            @PathVariable Long magacinId,
            @PathVariable Long proizvodId,
            @PathVariable Double kolicina) {
        return inventoryService.rezervisiRobu(magacinId, proizvodId, kolicina);
    }
    
    @PostMapping("/oslobodi/{magacinId}/{proizvodId}/{kolicina}")
    public void oslobodiRezervaciju(
            @PathVariable Long magacinId,
            @PathVariable Long proizvodId,
            @PathVariable Double kolicina) {
        inventoryService.oslobodiRezervaciju(magacinId, proizvodId, kolicina);
    }
    
    @PostMapping("/potvrdi/{magacinId}/{proizvodId}/{kolicina}")
    public void potvrdiRezervaciju(
            @PathVariable Long magacinId,
            @PathVariable Long proizvodId,
            @PathVariable Double kolicina) {
        inventoryService.potvrdiRezervaciju(magacinId, proizvodId, kolicina);
    }
}