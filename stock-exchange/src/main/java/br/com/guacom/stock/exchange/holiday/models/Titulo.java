package br.com.guacom.stock.exchange.holiday.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.guacom.stock.exchange.holiday.util.DescriptionDeserializer;
import br.com.guacom.stock.exchange.holiday.util.Messages;

@Entity
public class Titulo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonIgnore
	private Integer id;
	
	private String nome;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@JsonDeserialize(using = DescriptionDeserializer.class)
	@Column(length = 10000)
	private List<String> descricoes;

	public Titulo(Integer id, String nome, List<String> descricoes) {
		if(descricoes == null || descricoes.size() == 0)
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.id = id;
		this.nome = nome;
		this.descricoes = descricoes;
	}

	public Titulo(String nome, List<String> descricoes) {
		this(null, nome, descricoes);
	}
	
	public Titulo(List<String> descricoes) {
		this(null, descricoes);
	}
	
	public Titulo() {}
	
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<String> getDescricoes() {
		return Collections.unmodifiableList(descricoes);
	}
	
	public void setDescricoes(List<String> descricoes) {
		this.descricoes = descricoes;
	}
	
	@Override
	public String toString() {
		return "Title=" + nome + ", descricoes=" + descricoes + "";
	}
}