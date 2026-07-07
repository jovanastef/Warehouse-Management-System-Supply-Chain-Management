package poslovne.aplikacije.orders;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class StavkaPorudzbine implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "porudzbina_id")
    private Porudzbina porudzbina;
    
    private Long proizvodId;
    private String nazivProizvoda;
    private Double trazenaKolicina;
    private Double nabavnaCenaPoJedinici;
    private Double prodajnaCenaPoJedinici;
    private Double marza;
    
    public StavkaPorudzbine() {}
    
    public StavkaPorudzbine(Long proizvodId, String nazivProizvoda, 
                           Double trazenaKolicina, Double nabavnaCena, 
                           Double prodajnaCena) {
        this.proizvodId = proizvodId;
        this.nazivProizvoda = nazivProizvoda;
        this.trazenaKolicina = trazenaKolicina;
        this.nabavnaCenaPoJedinici = nabavnaCena;
        this.prodajnaCenaPoJedinici = prodajnaCena;
        this.marza = ((prodajnaCena - nabavnaCena) / nabavnaCena) * 100;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Porudzbina getPorudzbina() { return porudzbina; }
    public void setPorudzbina(Porudzbina porudzbina) { this.porudzbina = porudzbina; }
    
    public Long getProizvodId() { return proizvodId; }
    public void setProizvodId(Long proizvodId) { this.proizvodId = proizvodId; }
    
    public String getNazivProizvoda() { return nazivProizvoda; }
    public void setNazivProizvoda(String nazivProizvoda) { this.nazivProizvoda = nazivProizvoda; }
    
    public Double getTrazenaKolicina() { return trazenaKolicina; }
    public void setTrazenaKolicina(Double trazenaKolicina) { this.trazenaKolicina = trazenaKolicina; }
    
    public Double getNabavnaCenaPoJedinici() { return nabavnaCenaPoJedinici; }
    public void setNabavnaCenaPoJedinici(Double nabavnaCenaPoJedinici) { 
        this.nabavnaCenaPoJedinici = nabavnaCenaPoJedinici;
        if (this.prodajnaCenaPoJedinici != null && nabavnaCenaPoJedinici > 0) {
            this.marza = ((prodajnaCenaPoJedinici - nabavnaCenaPoJedinici) / nabavnaCenaPoJedinici) * 100;
        }
    }
    
    public Double getProdajnaCenaPoJedinici() { return prodajnaCenaPoJedinici; }
    public void setProdajnaCenaPoJedinici(Double prodajnaCenaPoJedinici) { 
        this.prodajnaCenaPoJedinici = prodajnaCenaPoJedinici;
        if (this.nabavnaCenaPoJedinici != null && this.nabavnaCenaPoJedinici > 0) {
            this.marza = ((prodajnaCenaPoJedinici - nabavnaCenaPoJedinici) / nabavnaCenaPoJedinici) * 100;
        }
    }
    
    public Double getMarza() { return marza; }
    public void setMarza(Double marza) { this.marza = marza; }
    
    public Double getUkupnaCenaStavke() {
        if (this.prodajnaCenaPoJedinici != null && this.trazenaKolicina != null) {
            return this.prodajnaCenaPoJedinici * this.trazenaKolicina;
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "StavkaPorudzbine [proizvodId=" + proizvodId + 
               ", naziv=" + nazivProizvoda + 
               ", kolicina=" + trazenaKolicina + 
               ", nabavnaCena=" + nabavnaCenaPoJedinici + 
               ", prodajnaCena=" + prodajnaCenaPoJedinici + 
               ", marza=" + marza + "%]";
    }
}