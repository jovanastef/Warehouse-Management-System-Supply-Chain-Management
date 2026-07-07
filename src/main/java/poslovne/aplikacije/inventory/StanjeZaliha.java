package poslovne.aplikacije.inventory;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import poslovne.aplikacije.proizvodi.Proizvod;

@Entity
public class StanjeZaliha implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Magacin magacin;
    
    @ManyToOne
    private Proizvod proizvod;
    
    private Double ukupnaKolicina = 0.0;
    private Double rezervisanaKolicina = 0.0;
    private Double raspolozivaKolicina = 0.0;
    
    public StanjeZaliha() {}
    
    public StanjeZaliha(Magacin magacin, Proizvod proizvod, Double kolicina) {
        this.magacin = magacin;
        this.proizvod = proizvod;
        this.ukupnaKolicina = kolicina;
        this.raspolozivaKolicina = kolicina;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Magacin getMagacin() { return magacin; }
    public void setMagacin(Magacin magacin) { this.magacin = magacin; }
    
    public Proizvod getProizvod() { return proizvod; }
    public void setProizvod(Proizvod proizvod) { this.proizvod = proizvod; }
    
    public Double getUkupnaKolicina() { return ukupnaKolicina; }
    public void setUkupnaKolicina(Double ukupnaKolicina) { 
        this.ukupnaKolicina = ukupnaKolicina;
        this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
    }
    
    public Double getRezervisanaKolicina() { return rezervisanaKolicina; }
    public void setRezervisanaKolicina(Double rezervisanaKolicina) { 
        this.rezervisanaKolicina = rezervisanaKolicina;
        this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
    }
    
    public Double getRaspolozivaKolicina() { return raspolozivaKolicina; }
    
    public void dodajKolicinu(Double kolicina) {
        this.ukupnaKolicina += kolicina;
        this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
    }
    
    public boolean rezervisi(Double kolicina) {
        if (this.raspolozivaKolicina >= kolicina) {
            this.rezervisanaKolicina += kolicina;
            this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
            return true;
        }
        return false;
    }
    
    public void oslobodiRezervaciju(Double kolicina) {
        if (this.rezervisanaKolicina >= kolicina) {
            this.rezervisanaKolicina -= kolicina;
            this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
        }
    }
    
    public void ukloniRezervisano(Double kolicina) {
        if (this.rezervisanaKolicina >= kolicina && this.ukupnaKolicina >= kolicina) {
            this.ukupnaKolicina -= kolicina;
            this.rezervisanaKolicina -= kolicina;
            this.raspolozivaKolicina = ukupnaKolicina - rezervisanaKolicina;
        }
    }
    
    @Override
    public String toString() {
        return "StanjeZaliha [magacin=" + magacin.getSifraMagacina() + 
               ", proizvod=" + proizvod.getNaziv() + 
               ", ukupno=" + ukupnaKolicina + 
               ", rezervisano=" + rezervisanaKolicina + 
               ", raspolozivo=" + raspolozivaKolicina + "]";
    }
}