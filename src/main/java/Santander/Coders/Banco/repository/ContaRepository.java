package Santander.Coders.Banco.repository;

import Santander.Coders.Banco.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    @Query(
            "SELECT c FROM Conta c WHERE c.canceled = false"
    )
    Page<Conta> findAllAndCanceledFalse(Pageable pageable);

    Optional<Conta> findByIdAndCanceledFalse(Long id);

    Conta findByNumero(Long numero);

    boolean existsByNumero(Long numero);
}
