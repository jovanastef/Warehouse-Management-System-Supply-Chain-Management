package poslovne.aplikacije.orders;

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
@RequestMapping("/orders")
public class OrdersController {
    
    @Autowired
    private OrdersService ordersService;
    
    // PORUDZBINA ENDPOINTS =====
    
    @GetMapping("/porudzbine")
    public List<Porudzbina> getAllPorudzbine() {
        return ordersService.getAllPorudzbine();
    }
    
    @GetMapping("/porudzbine/{id}")
    public Porudzbina getPorudzbinaById(@PathVariable Long id) {
        return ordersService.getPorudzbinaById(id)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));
    }
    
    @GetMapping("/porudzbine/sifra/{sifra}")
    public Porudzbina getPorudzbinaBySifra(@PathVariable String sifra) {
        return ordersService.getPorudzbinaBySifra(sifra)
            .orElseThrow(() -> new RuntimeException("Porudzbina nije pronadjena"));
    }
    
    @GetMapping("/porudzbine/kupac/{sifraKupca}")
    public List<Porudzbina> getPorudzbineByKupac(@PathVariable String sifraKupca) {
        return ordersService.getPorudzbineByKupac(sifraKupca);
    }
    
    @GetMapping("/porudzbine/status/{status}")
    public List<Porudzbina> getPorudzbineByStatus(@PathVariable StatusPorudzbine status) {
        return ordersService.getPorudzbineByStatus(status);
    }
    
    @PostMapping("/porudzbine")
    public Porudzbina createOrder(@RequestBody Porudzbina porudzbina) {
        return ordersService.createOrder(porudzbina);
    }
    
    @PostMapping("/porudzbine/{id}/confirm")
    public Porudzbina confirmOrder(@PathVariable Long id) {
        return ordersService.confirmOrder(id);
    }
    
    @PostMapping("/porudzbine/{id}/cancel")
    public Porudzbina cancelOrder(@PathVariable Long id) {
        return ordersService.cancelOrder(id);
    }
    
    @PostMapping("/porudzbine/{id}/complete")
    public Porudzbina completeOrder(@PathVariable Long id) {
        return ordersService.completeOrder(id);
    }
}