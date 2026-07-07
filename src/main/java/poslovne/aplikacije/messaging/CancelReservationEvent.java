package poslovne.aplikacije.messaging;

import java.io.Serializable;
import java.util.List;

public class CancelReservationEvent implements Serializable {
    private Long porudzbinaId;
    private List<Long> proizvodIds;
    private List<Double> kolicine;

    public CancelReservationEvent() {}

    public CancelReservationEvent(Long porudzbinaId, List<Long> proizvodIds, List<Double> kolicine) {
        this.porudzbinaId = porudzbinaId;
        this.proizvodIds = proizvodIds;
        this.kolicine = kolicine;
    }

    // Getters and Setters
    public Long getPorudzbinaId() { return porudzbinaId; }
    public void setPorudzbinaId(Long porudzbinaId) { this.porudzbinaId = porudzbinaId; }
    public List<Long> getProizvodIds() { return proizvodIds; }
    public void setProizvodIds(List<Long> proizvodIds) { this.proizvodIds = proizvodIds; }
    public List<Double> getKolicine() { return kolicine; }
    public void setKolicine(List<Double> kolicine) { this.kolicine = kolicine; }
}