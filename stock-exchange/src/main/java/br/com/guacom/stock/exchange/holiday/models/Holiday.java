package br.com.guacom.stock.exchange.holiday.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.guacom.stock.exchange.holiday.util.Messages;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Holiday {
	@JsonIgnore
	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonAlias("dia")
	private Integer day;
	
	@JsonAlias("mes")
	private String month;
	
	@JsonAlias("evento")
	private String event;

	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = false)
	@JoinColumn(name="id_holiday")
	@JsonAlias("titulos")
	private List<Title> titles;
	
	public Holiday(Integer id, Integer day, String month, String event, List<Title> titles) {
		if(day == null || event.isBlank() || month.isBlank() || titles == null || titles.size() == 0)
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.id = id;
		this.day = day;
		this.month = month;
		this.event = event;
		this.titles = titles;
	}
	
	public Holiday(Integer dia, String mes, String evento, List<Title> titulos) {
		this(null, dia, mes, evento, titulos);
	}
	
	public Holiday(Integer dia, String mes, String evento) {
		this.day = dia;
		this.month = mes;
		this.event = evento;
	}

	public Holiday() {}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getDay() {
		return day;
	}
	
	public void setDay(Integer day) {
		this.day = day;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public List<Title> getTitles() {
		return titles;
	}
	
	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}
	
	@Override
	public String toString() {
		return "Dia: " + day + ", Mês: " + month + ", feriado - " + event + ", títulos " + titles;
	}
}