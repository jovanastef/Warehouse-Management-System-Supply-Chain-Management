package poslovne.aplikacije.orders;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PorudzbinaRepository extends JpaRepository<Porudzbina, Long> {
    public List<Porudzbina> findAll();
    public Optional<Porudzbina> findBySifraPorudzbine(String sifraPorudzbine);
    public List<Porudzbina> findBySifraKupca(String sifraKupca);
    public List<Porudzbina> findByStatus(StatusPorudzbine status);
}