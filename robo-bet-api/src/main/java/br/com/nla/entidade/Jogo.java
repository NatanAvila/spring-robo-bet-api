package br.com.nla.entidade;

import java.util.Set;

import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jogo {

	private Set<Time> times;
	
	private String url;
	
	private String titulo;
	
	@EqualsAndHashCode.Exclude
	private boolean completo;
	
	public Jogo (JSONObject json) {
		this.url = json.getString("url");
		this.titulo = json.getString("name");
		completo = false;
	}
	
	
}
