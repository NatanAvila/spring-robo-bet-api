package br.com.nla.entidade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity @Data
@Table(name = "mercado")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Mercado extends Entidade {

	private static final long serialVersionUID = -2436708335955450614L;

	private String nome;

	@OneToMany(mappedBy = "mercado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Odd> odds;
	
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jogo")
	private Jogo jogo;

	public Mercado(JSONObject json, Jogo jogo) {
		this.nome = json.getString("name");
		this.jogo = jogo;
		odds = new HashSet<Odd>();
		for (var obj : json.getJSONArray("selections")) {
			odds.add(new Odd(new JSONObject(obj.toString()), this));
		}
	}
}
