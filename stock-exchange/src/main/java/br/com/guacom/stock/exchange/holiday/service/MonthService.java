package br.com.guacom.stock.exchange.holiday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guacom.stock.exchange.holiday.models.Mes;
import br.com.guacom.stock.exchange.holiday.repository.MonthRepository;

@Service
public class MonthService {
	
	@Autowired
	private MonthRepository repository;
	
	public void save(Mes mes) {
		repository.save(mes);
	}
	
	public void saveAll(List<Mes> meses) {
		repository.saveAll(meses);
	}
	
	public List<Mes> findAll() {
		return repository.findAll();
	}
	
	public Mes findById(Long id) {
		return repository.findById(id).get();
	}
	
	public void merge(Mes mes) {
		repository.save(mes);
	}
	
	public void delete(Mes mes) {
		repository.delete(mes);
	}
}