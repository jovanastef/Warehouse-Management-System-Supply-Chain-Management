package poslovne.aplikacije.exceptions;

public class ProizvodNotFoundException extends RuntimeException {
    public ProizvodNotFoundException(Long id) {
        super("Ne postoji proizvod sa id-jem:" + id);
    }
}