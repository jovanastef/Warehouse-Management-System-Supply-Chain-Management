package poslovne.aplikacije.orders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

@Entity
public class Porudzbina implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String sifraPorudzbine;
    private LocalDate datumKreiranja;
    private String sifraKupca;
    private String nazivKupca;
    private String adresaIsporuke;
    private String gradIsporuke;
    private Double ukupanIznos = 0.0;
    
    private LocalDate rokZaUplatu;
    
    @Enumerated(EnumType.STRING)
    private StatusPorudzbine status = StatusPorudzbine.KREIRANA;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "porudzbina")
    private List<StavkaPorudzbine> stavke = new ArrayList<>();
    
    public Porudzbina() {}
    
    public Porudzbina(String sifraPorudzbine, String sifraKupca, String nazivKupca, 
                     String adresaIsporuke, String gradIsporuke) {
        this.sifraPorudzbine = sifraPorudzbine;
        this.datumKreiranja = LocalDate.now();
        this.sifraKupca = sifraKupca;
        this.nazivKupca = nazivKupca;
        this.adresaIsporuke = adresaIsporuke;
        this.gradIsporuke = gradIsporuke;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSifraPorudzbine() { return sifraPorudzbine; }
    public void setSifraPorudzbine(String sifraPorudzbine) { this.sifraPorudzbine = sifraPorudzbine; }
    
    public LocalDate getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(LocalDate datumKreiranja) { this.datumKreiranja = datumKreiranja; }
    
    public String getSifraKupca() { return sifraKupca; }
    public void setSifraKupca(String sifraKupca) { this.sifraKupca = sifraKupca; }
    
    public String getNazivKupca() { return nazivKupca; }
    public void setNazivKupca(String nazivKupca) { this.nazivKupca = nazivKupca; }
    
    public String getAdresaIsporuke() { return adresaIsporuke; }
    public void setAdresaIsporuke(String adresaIsporuke) { this.adresaIsporuke = adresaIsporuke; }
    
    public String getGradIsporuke() { return gradIsporuke; }
    public void setGradIsporuke(String gradIsporuke) { this.gradIsporuke = gradIsporuke; }
    
    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }
    
    public LocalDate getRokZaUplatu() { return rokZaUplatu; }
    public void setRokZaUplatu(LocalDate rokZaUplatu) { this.rokZaUplatu = rokZaUplatu; }
    
    public StatusPorudzbine getStatus() { return status; }
    public void setStatus(StatusPorudzbine status) { this.status = status; }
    
    public List<StavkaPorudzbine> getStavke() { return stavke; }
    public void setStavke(List<StavkaPorudzbine> stavke) { 
        this.stavke = stavke;
        this.izracunajUkupanIznos();
    }
    
    public void dodajStavku(StavkaPorudzbine stavka) {
        this.stavke.add(stavka);
        stavka.setPorudzbina(this);
        this.izracunajUkupanIznos();
    }
    
    private void izracunajUkupanIznos() {
        this.ukupanIznos = this.stavke.stream()
            .mapToDouble(StavkaPorudzbine::getUkupnaCenaStavke)
            .sum();
    }
    
    public void azurirajStatus(StatusPorudzbine status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Porudzbina [sifra=" + sifraPorudzbine + 
               ", kupac=" + nazivKupca + 
               ", status=" + status + 
               ", ukupanIznos=" + ukupanIznos + 
               ", brojStavki=" + stavke.size() + "]";
    }
}