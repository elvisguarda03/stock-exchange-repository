package br.com.guacom.stock.exchange.holiday.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.guacom.stock.exchange.holiday.util.Messages;

@Entity
public class Mes implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "mes")
	private String mes;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_mes")
	private List<Feriado> feriados;

	public Mes(Integer id, String mes, List<Feriado> feriados) {
		if(mes.isBlank() || feriados == null)
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.id = id;
		this.mes = mes;
		this.feriados = feriados;
	}

	public Mes(String mes, List<Feriado> feriados) {
		this(null, mes, feriados);
	}
	
	public Mes(String mes) {
		if(mes.isBlank())
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.mes = mes;
	}
	public Mes() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public List<Feriado> getFeriados() {
		return feriados;
	}

	public void setFeriados(List<Feriado> feriados) {
		this.feriados = feriados;
	}
	
	@Override
	public String toString() {
		return "id = " + id + ", mes" + mes + ", feriados " + feriados;
	}
}