package Santander.Coders.Banco.repository;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    @Query(
            "SELECT p FROM Pessoa p WHERE p.inatived = false"
    )
    Page<Pessoa> findAllAndInativedFalse(Pageable pageable);

    Optional<Pessoa> findByIdAndInativedFalse(Long id);

    boolean existsByCpf(String cpf);
}
