package br.com.nla.entidade;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Odd {

	private String nome;
	private Double valor;
	private Double handicap;

	public Odd(JSONObject json) {
		if (json.has("fullName")) {
			this.nome = json.getString("fullName");
		} else {
			this.nome = json.getString("name");
		}

		this.valor = json.getDouble("price");
		this.handicap = json.optDouble("handicap", 0);
	}
}
