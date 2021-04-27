package br.com.nla.entidade;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public class Entidade implements Serializable{

	private static final long serialVersionUID = -2519538288738425858L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long codigo;
	
}
