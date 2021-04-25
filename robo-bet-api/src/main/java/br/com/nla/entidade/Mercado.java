package br.com.nla.entidade;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mercado {

	private String nome;

	private Set<Odd> odds;

	public Mercado(JSONObject json) {
		this.nome = json.getString("name");
		odds = new HashSet<Odd>();
		for (var obj : json.getJSONArray("selections")) {
			odds.add(new Odd(new JSONObject(obj.toString())));
		}
	}
}
