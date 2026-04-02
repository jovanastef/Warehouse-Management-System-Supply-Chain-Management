package poslovne.aplikacije.exceptions;

public class ProizvodIsNullException extends RuntimeException {
    public ProizvodIsNullException() {
        super("Ne postoje podaci o proizvodu");
    }
}