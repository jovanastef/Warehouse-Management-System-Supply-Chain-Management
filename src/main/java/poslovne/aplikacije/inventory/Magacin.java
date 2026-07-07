package poslovne.aplikacije.inventory;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Magacin implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String sifraMagacina;
    private String naziv;
    private String adresa;
    private String grad;
    private boolean aktivan = true;
    
    public Magacin() {}
    
    public Magacin(String sifraMagacina, String naziv, String adresa, String grad) {
        this.sifraMagacina = sifraMagacina;
        this.naziv = naziv;
        this.adresa = adresa;
        this.grad = grad;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSifraMagacina() { return sifraMagacina; }
    public void setSifraMagacina(String sifraMagacina) { this.sifraMagacina = sifraMagacina; }
    
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    
    public String getGrad() { return grad; }
    public void setGrad(String grad) { this.grad = grad; }
    
    public boolean isAktivan() { return aktivan; }
    public void setAktivan(boolean aktivan) { this.aktivan = aktivan; }
    
    @Override
    public String toString() {
        return "Magacin [id=" + id + ", sifra=" + sifraMagacina + ", naziv=" + naziv + "]";
    }
}