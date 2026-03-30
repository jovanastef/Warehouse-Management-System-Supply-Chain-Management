package poslovne.aplikacije.messaging;

import java.io.Serializable;
import poslovne.aplikacije.proizvodi.Proizvod;

public class ProizvodEvent implements Serializable {
    public static enum ProizvodEventType { 
        NONE, NEW_PROIZVOD, DELETED_PROIZVOD, UPDATED_PROIZVOD 
    }
    
    private ProizvodEventType eventType = ProizvodEventType.NONE;
    private Proizvod proizvod = null;
    
    public static ProizvodEvent createNewProizvodEvent(Proizvod noviProizvod) {
        return new ProizvodEvent(ProizvodEventType.NEW_PROIZVOD, noviProizvod);
    }
    
    public static ProizvodEvent createDeletedProizvodEvent(Proizvod deletedProizvod) {
        return new ProizvodEvent(ProizvodEventType.DELETED_PROIZVOD, deletedProizvod);
    }
    
    public static ProizvodEvent createUpdatedProizvodEvent(Proizvod updatedProizvod) {
        return new ProizvodEvent(ProizvodEventType.UPDATED_PROIZVOD, updatedProizvod);
    }
    
    ProizvodEvent(ProizvodEventType eventType, Proizvod proizvod) {
        this.eventType = eventType;
        this.proizvod = proizvod;
    }
    
    public ProizvodEventType getEventType() { return eventType; }
    public Proizvod getProizvod() { return proizvod; }
}