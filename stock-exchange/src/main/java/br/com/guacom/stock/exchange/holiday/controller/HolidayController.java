package br.com.guacom.stock.exchange.holiday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.guacom.stock.exchange.holiday.service.MonthService;

@Controller
public class HolidayController {

	@Autowired
	private MonthService service;
//	Terça-feira seleção senai
//	Horário - 13:30
//	Cimatec 2 - 3° andar Lab software

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("months", service.findAll());
		return "index";
	}

	@GetMapping("/accordion")
	public String teste(Model model) {
//		CalendarScraping scraping = new CalendarScraping();
		try {
//			scraping.buildJsonThroughTheDataInTheTag();
//			ArrayNode arrayNode = scraping.getJsonNode();
//			HolidayJson json = new HolidayJson();
//			List<Month> months = json.fromJson(arrayNode);
//			for (Month m : months)
//				System.out.println(m);
			model.addAttribute("months", service.findAll());
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
		return "teste";
	}
}