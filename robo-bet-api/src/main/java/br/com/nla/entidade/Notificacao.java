package br.com.nla.entidade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "notificacao")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Notificacao extends Entidade {

	private static final long serialVersionUID = -8815954693420020007L;

	private Long chat;

	@EqualsAndHashCode.Exclude
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "notificacao_jogo", joinColumns = @JoinColumn(name = "notificacao"), inverseJoinColumns = @JoinColumn(name = "jogo"))
	private Set<Jogo> jogos;

	public Notificacao(Long chat) {
		this.chat = chat;
		jogos = new HashSet<>();
	}
}
