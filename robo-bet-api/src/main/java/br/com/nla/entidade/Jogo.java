package br.com.nla.entidade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity @Data
@Table(name = "jogo")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Jogo extends Entidade {

	private static final long serialVersionUID = 2649538320814821899L;

	private String url;

	private String titulo;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "jogo", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Mercado> mercados;

	public Jogo(JSONObject json) {
		this.url = json.getString("url");
		this.titulo = json.getString("name");
		this.mercados = new HashSet<>();
	}

}
