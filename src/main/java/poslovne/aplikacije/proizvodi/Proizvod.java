package poslovne.aplikacije.proizvodi;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Proizvod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String naziv;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "smallint")
    private KategorijeProizvoda kat = KategorijeProizvoda.NEKATEGORISAN;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "smallint")
    private JedinicaMere jm;
    
    private double kolicinaMere;
    private double jedinicnaCena = 0.0;
    private Valuta valuta = Valuta.RSD;
    
    public Proizvod(long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }
    
    protected Proizvod() {}
    
    public Long getId() { return id; }
    public String getNaziv() { return naziv; }
    public KategorijeProizvoda getKat() { return kat; }
    public void setKat(KategorijeProizvoda kat) { this.kat = kat; }
    public JedinicaMere getJm() { return jm; }
    public void setJm(JedinicaMere jm) { this.jm = jm; }
    public double getJedinicnaCena() { return jedinicnaCena; }
    public void setJedinicnaCena(double jedinicnaCena) { this.jedinicnaCena = jedinicnaCena; }
    public Valuta getValuta() { return valuta; }
    public void setValuta(Valuta valuta) { this.valuta = valuta; }
    public double getKolicinaMere() { return kolicinaMere; }
    public void setKolicinaMere(double kolicinaMere) { this.kolicinaMere = kolicinaMere; }
    
    public Proizvod updateProizvod(Proizvod proizvod) {
        this.naziv = proizvod.naziv;
        this.jm = proizvod.jm;
        this.jedinicnaCena = proizvod.jedinicnaCena;
        this.kolicinaMere = proizvod.kolicinaMere;
        this.valuta = proizvod.valuta;
        return this;
    }
}