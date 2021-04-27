package br.com.nla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.nla.entidade.Jogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {

	@Query(value = "DELETE FROM jogo WHERE codigo IN (:jogos)", nativeQuery= true)
	void deletarJogos(List<Jogo> jogos);
}
