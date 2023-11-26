package Santander.Coders.Banco.repository;

import Santander.Coders.Banco.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
