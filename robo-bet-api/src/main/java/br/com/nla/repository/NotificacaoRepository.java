package br.com.nla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.nla.entidade.Notificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long>{

}
