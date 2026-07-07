package poslovne.aplikacije.orders;

public enum StatusPorudzbine {
    KREIRANA,           // Porudzbina je kreirana
    REZERVISANA,        // Roba je rezervisana
    POTVRDJENA,         // Porudzbina je potvrdjena
    U_PRIPREMI,         // Porudzbina se priprema za isporuku
    POSLATA,            // Porudzbina je poslata
    ISPORUCENA,         // Porudzbina je isporucena
    OTKAZANA            // Porudzbina je otkazana
}