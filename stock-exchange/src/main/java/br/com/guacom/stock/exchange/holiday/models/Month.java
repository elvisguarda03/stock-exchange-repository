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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.guacom.stock.exchange.holiday.util.Messages;

@Entity
public class Month implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue
	private Integer id;

	@JsonAlias("mes")
	@Column(name = "mes")
	private String month;

	@JsonAlias("feriados")
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_mes")
	private List<Holiday> holidays;

	public Month(Integer id, String month, List<Holiday> holidays) {
		if(month.isBlank() || holidays == null)
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.id = id;
		this.month = month;
		this.holidays = holidays;
	}

	public Month(String month, List<Holiday> holidays) {
		this(null, month, holidays);
	}
	
	public Month(String month) {
		if(month.isBlank())
			throw new IllegalArgumentException(Messages.MSG_1.getMessage());
		this.month = month;
	}
	public Month() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}
}