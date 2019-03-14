package br.com.guacom.stock.exchange.holiday.models;

import java.util.Collections;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.guacom.stock.exchange.holiday.util.DescriptionDeserializer;

@Entity
public class Title {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Integer id;
	
	@JsonAlias("nome")
	private String name;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@JsonAlias("descricoes")
	@JsonDeserialize(using = DescriptionDeserializer.class)
	private List<String> descriptions;

	public Title(Integer id, String name, List<String> descriptions) {
		if(descriptions == null || descriptions.size() == 0)
			throw new IllegalArgumentException();
		this.id = id;
		this.name = name;
		this.descriptions = descriptions;
	}

	public Title(String name, List<String> descriptions) {
		this(null, name, descriptions);
	}
	
	public Title(List<String> descriptions) {
		this(null, descriptions);
	}
	
	public Title() {}
	
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getDescriptions() {
		return Collections.unmodifiableList(descriptions);
	}
	
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	
	@Override
	public String toString() {
		return "Title=" + name + ", descricoes=" + descriptions + "";
	}
}