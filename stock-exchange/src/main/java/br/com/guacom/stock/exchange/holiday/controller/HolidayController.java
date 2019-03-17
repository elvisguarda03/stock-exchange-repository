package br.com.guacom.stock.exchange.holiday.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import br.com.guacom.stock.exchange.holiday.json.adapter.HolidayJson;
import br.com.guacom.stock.exchange.holiday.models.CalendarScraping;
import br.com.guacom.stock.exchange.holiday.models.Mes;
import br.com.guacom.stock.exchange.holiday.service.MonthService;

@Controller
public class HolidayController {

	@Autowired
	private MonthService service;

	@GetMapping("/")
	public String index(Model model) {
		List<Mes> findAll = service.findAll();
		if (findAll.size() > 0)
			model.addAttribute("months", findAll);
		else {
			CalendarScraping scraping = new CalendarScraping();
			try {
				List<Mes> months = null;
				scraping.buildJsonThroughTheDataInTheTag();
				ArrayNode arrayNode = scraping.getJsonNode();
				HolidayJson json = new HolidayJson();
				months = json.fromJson(arrayNode);
				service.saveAll(months);
				model.addAttribute("months", months);
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
		return "index";
	}

	@GetMapping("/json")
	public String json(Model model) {
		CalendarScraping scraping = new CalendarScraping();
		try {
			List<String> jsons = new ArrayList<>();
			scraping.buildJsonThroughTheDataInTheTag();
			ArrayNode arrayNode = scraping.getJsonNode();
			HolidayJson json = new HolidayJson();
			for (JsonNode jsonNode : arrayNode)
				jsons.add(json.prettyPrintJsonString(jsonNode));
			model.addAttribute("jsons", jsons);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
		return "json";
	}
}