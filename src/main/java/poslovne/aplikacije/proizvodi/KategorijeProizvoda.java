package poslovne.aplikacije.proizvodi;

import java.util.stream.Stream;

public enum KategorijeProizvoda {
    NEKATEGORISAN(0), NAMIRNICE(1), MESO(2), VOCE(3), POVRCE(4), 
    SLATKISI(5), BEZALKOHOLNA_PICA(6), ALKOHOLNA_PICA(7), 
    LICNA_HIGIJENA(8), KUCNA_HEMIJA(9);
    
    private int kat = 0;
    
    KategorijeProizvoda(int kat){
        this.kat = kat;
    }
    
    KategorijeProizvoda(){}
    
    public int getKat() {
        return kat;
    }
    
    public void setKat(int kat) {
        this.kat = kat;
    }
    
    public static KategorijeProizvoda of(int kat) {
        return Stream.of(KategorijeProizvoda.values())
            .filter(k -> k.getKat() == kat)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}