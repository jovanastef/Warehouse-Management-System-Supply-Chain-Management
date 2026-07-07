package poslovne.aplikacije.messaging;

import java.io.Serializable;
import java.time.LocalDate;

public class PaymentConfirmedEvent implements Serializable {
    private Long porudzbinaId;
    private LocalDate datumPlacanja;
    private Double iznos;

    public PaymentConfirmedEvent() {}

    public PaymentConfirmedEvent(Long porudzbinaId, Double iznos) {
        this.porudzbinaId = porudzbinaId;
        this.datumPlacanja = LocalDate.now();
        this.iznos = iznos;
    }

    // Getters and Setters
    public Long getPorudzbinaId() { return porudzbinaId; }
    public void setPorudzbinaId(Long porudzbinaId) { this.porudzbinaId = porudzbinaId; }
    public LocalDate getDatumPlacanja() { return datumPlacanja; }
    public void setDatumPlacanja(LocalDate datumPlacanja) { this.datumPlacanja = datumPlacanja; }
    public Double getIznos() { return iznos; }
    public void setIznos(Double iznos) { this.iznos = iznos; }
}