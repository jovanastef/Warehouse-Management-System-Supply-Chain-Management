package poslovne.aplikacije.inventory;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagacinRepository extends JpaRepository<Magacin, Long> {
    public List<Magacin> findAll();
    public Optional<Magacin> findBySifraMagacina(String sifraMagacina);
    public List<Magacin> findByAktivanTrue();
}