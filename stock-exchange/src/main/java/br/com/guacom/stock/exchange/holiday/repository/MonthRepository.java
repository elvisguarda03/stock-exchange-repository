package br.com.guacom.stock.exchange.holiday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guacom.stock.exchange.holiday.models.Month;

public interface MonthRepository extends JpaRepository<Month, Long> {

}
