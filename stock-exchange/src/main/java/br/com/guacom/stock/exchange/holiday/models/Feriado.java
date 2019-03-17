package br.com.guacom.stock.exchange.holiday.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.guacom.stock.exchange.holiday.util.Messages;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Feriado implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue
	private Integer id;
	
	private Integer dia;
	
	private String evento;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@JoinColumn(name="id_holiday")
	private List<Titulo> titulos;
	
	public Feriado(Integer id, Integer dia, String evento, List<Titulo> titulos) {
		if(dia == null || evento.isBlank()|| titulos == null || titulos.size() == 0)
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.id = id;
		this.dia = dia;
		this.evento = evento;
		this.titulos = titulos;
	}
	
	public Feriado(Integer dia, String evento, List<Titulo> titulos) {
		this(null, dia, evento, titulos);
	}
	
	public Feriado(Integer dia, String evento) {
		if(dia == null || evento.isBlank())
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.dia = dia;
		this.evento = evento;
	}

	public Feriado() {}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getDia() {
		return dia;
	}
	
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	
	public String getEvento() {
		return evento;
	}
	
	public void setEvento(String evento) {
		this.evento = evento;
	}
	
	public List<Titulo> getTitulos() {
		return titulos;
	}
	
	public void setTitulos(List<Titulo> titulos) {
		this.titulos = titulos;
	}
	
	@Override
	public String toString() {
		return "Dia: " + dia + ", feriado - " + evento + ", títulos " + titulos;
	}
}