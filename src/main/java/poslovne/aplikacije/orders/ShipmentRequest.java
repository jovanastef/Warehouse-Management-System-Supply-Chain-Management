package poslovne.aplikacije.orders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentRequest implements Serializable {
    
    private Long id;
    private Long porudzbinaId;
    private String sifraPorudzbine;
    private List<StavkaPorudzbine> stavke = new ArrayList<>();
    private Double ukupanIznos;
    private LocalDate rokZaUplatu;
    private String status; // "CEKA_PLACANJE", "SPREMAN_ZA_ISPORUKU"
    
    public ShipmentRequest() {}
    
    public ShipmentRequest(Porudzbina porudzbina) {
        this.id = porudzbina.getId();
        this.porudzbinaId = porudzbina.getId();
        this.sifraPorudzbine = porudzbina.getSifraPorudzbine();
        this.stavke = porudzbina.getStavke();
        this.ukupanIznos = porudzbina.getUkupanIznos();
        this.rokZaUplatu = LocalDate.now().plusDays(8); // Rok od 8 dana
        this.status = "CEKA_PLACANJE";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPorudzbinaId() { return porudzbinaId; }
    public void setPorudzbinaId(Long porudzbinaId) { this.porudzbinaId = porudzbinaId; }
    
    public String getSifraPorudzbine() { return sifraPorudzbine; }
    public void setSifraPorudzbine(String sifraPorudzbine) { this.sifraPorudzbine = sifraPorudzbine; }
    
    public List<StavkaPorudzbine> getStavke() { return stavke; }
    public void setStavke(List<StavkaPorudzbine> stavke) { this.stavke = stavke; }
    
    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }
    
    public LocalDate getRokZaUplatu() { return rokZaUplatu; }
    public void setRokZaUplatu(LocalDate rokZaUplatu) { this.rokZaUplatu = rokZaUplatu; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "ShipmentRequest [porudzbinaId=" + porudzbinaId + 
               ", sifra=" + sifraPorudzbine + 
               ", ukupanIznos=" + ukupanIznos + 
               ", rokZaUplatu=" + rokZaUplatu + 
               ", status=" + status + "]";
    }
}