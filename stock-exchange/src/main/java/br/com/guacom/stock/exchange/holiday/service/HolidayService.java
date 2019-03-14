package br.com.guacom.stock.exchange.holiday.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guacom.stock.exchange.holiday.models.Holiday;
import br.com.guacom.stock.exchange.holiday.repository.HolidayRepository;

@Service
public class HolidayService {
	
	@Autowired
	private HolidayRepository repository;
	
	public void save(Holiday holiday) {
		repository.save(holiday);
	}
	
	public List<Holiday> findAll() {
		return repository.findAll();
	}
	
	public Holiday findById(Long id) {
		Optional<Holiday> holiday = repository.findById(id);
		return holiday.get();
	}
	
	public void merge(Holiday holiday) {
		repository.save(holiday);
	}
	
	public void delete(Holiday holiday) {
		repository.delete(holiday);;
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public void deleteAll() {
		repository.deleteAll();
	}
}