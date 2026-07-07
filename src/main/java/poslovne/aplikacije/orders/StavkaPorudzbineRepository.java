package poslovne.aplikacije.orders;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StavkaPorudzbineRepository extends JpaRepository<StavkaPorudzbine, Long> {
    public List<StavkaPorudzbine> findAll();
    public List<StavkaPorudzbine> findByProizvodId(Long proizvodId);
}