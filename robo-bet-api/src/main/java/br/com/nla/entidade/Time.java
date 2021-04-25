package br.com.nla.entidade;

import br.com.nla.util.enumarated.MandanteEnum;
import lombok.Data;

@Data
public class Time {

	private String nome;
	
	private MandanteEnum mandante;
	
}
