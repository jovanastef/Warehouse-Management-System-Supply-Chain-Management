package poslovne.aplikacije.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poslovne.aplikacije.proizvodi.KategorijeProizvoda;
import poslovne.aplikacije.proizvodi.Proizvod;

@Repository
public interface ProizvodRepository extends JpaRepository<Proizvod, Long> {
    public List<Proizvod> findAll();
    public List<Proizvod> findByKat(KategorijeProizvoda kat);
    public Proizvod save(Proizvod noviProizvod);
    public Optional<Proizvod> findById(long id);
    public void deleteById(Long id);
}