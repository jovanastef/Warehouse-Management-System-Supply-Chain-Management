package poslovne.aplikacije.messaging;

import java.io.Serializable;
import java.util.List;

public class GoodsSoldEvent implements Serializable {
    private Long porudzbinaId;
    private List<Long> proizvodIds;
    private List<Double> kolicine;
    private Long magacinId; // Iz kog magacina se izdaje

    public GoodsSoldEvent() {}

    public GoodsSoldEvent(Long porudzbinaId, List<Long> proizvodIds, List<Double> kolicine, Long magacinId) {
        this.porudzbinaId = porudzbinaId;
        this.proizvodIds = proizvodIds;
        this.kolicine = kolicine;
        this.magacinId = magacinId;
    }

    // Getters and Setters
    public Long getPorudzbinaId() { return porudzbinaId; }
    public void setPorudzbinaId(Long porudzbinaId) { this.porudzbinaId = porudzbinaId; }
    public List<Long> getProizvodIds() { return proizvodIds; }
    public void setProizvodIds(List<Long> proizvodIds) { this.proizvodIds = proizvodIds; }
    public List<Double> getKolicine() { return kolicine; }
    public void setKolicine(List<Double> kolicine) { this.kolicine = kolicine; }
    public Long getMagacinId() { return magacinId; }
    public void setMagacinId(Long magacinId) { this.magacinId = magacinId; }
}