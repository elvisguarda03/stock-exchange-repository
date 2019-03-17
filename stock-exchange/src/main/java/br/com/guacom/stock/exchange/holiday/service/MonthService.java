package br.com.guacom.stock.exchange.holiday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guacom.stock.exchange.holiday.models.Month;
import br.com.guacom.stock.exchange.holiday.repository.MonthRepository;

@Service
public class MonthService {
	
	@Autowired
	private MonthRepository repository;
	
	public void save(Month month) {
		repository.save(month);
	}
	
	public void saveAll(List<Month> months) {
		repository.saveAll(months);
	}
	
	public List<Month> findAll() {
		return repository.findAll();
	}
	
	public Month findById(Long id) {
		return repository.findById(id).get();
	}
	
	public void merge(Month month) {
		repository.save(month);
	}
	
	public void delete(Month month) {
		repository.delete(month);;
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public void deleteAll() {
		repository.deleteAll();
	}
}