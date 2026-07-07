package poslovne.aplikacije.inventory;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poslovne.aplikacije.proizvodi.Proizvod;

@Repository
public interface StanjeZalihaRepository extends JpaRepository<StanjeZaliha, Long> {
    public List<StanjeZaliha> findAll();
    public List<StanjeZaliha> findByMagacin(Magacin magacin);
    public List<StanjeZaliha> findByProizvod(Proizvod proizvod);
    public Optional<StanjeZaliha> findByMagacinAndProizvod(Magacin magacin, Proizvod proizvod);
}