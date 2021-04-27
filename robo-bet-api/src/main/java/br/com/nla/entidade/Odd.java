package br.com.nla.entidade;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "odd")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Odd extends Entidade {

	private static final long serialVersionUID = 7151359505908993586L;

	private String nome;
	private Double valor;
	private Double handicap;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mercado")
	private Mercado mercado;

	public Odd(JSONObject json, Mercado mercado) {
		if (json.has("fullName")) {
			this.nome = json.getString("fullName");
		} else {
			this.nome = json.getString("name");
		}

		this.mercado = mercado;
		this.valor = json.getDouble("price");
		this.handicap = json.optDouble("handicap", 0);
	}
}
