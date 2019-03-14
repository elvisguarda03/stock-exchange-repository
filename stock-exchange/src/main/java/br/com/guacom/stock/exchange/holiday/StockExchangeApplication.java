package br.com.guacom.stock.exchange.holiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "br.com.guacom.stock.exchange.holiday.models")
@EnableJpaRepositories(basePackages = "br.com.guacom.stock.exchange.holiday.repository")
@SpringBootApplication(scanBasePackages = "br.com.guacom.stock.exchange.holiday")
public class StockExchangeApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);
	}
}