package br.com.guacom.stock.exchange.holiday.json.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.guacom.stock.exchange.holiday.models.Holiday;
import br.com.guacom.stock.exchange.holiday.models.Month;
import br.com.guacom.stock.exchange.holiday.models.Title;
import br.com.guacom.stock.exchange.holiday.util.Key;
import br.com.guacom.stock.exchange.holiday.util.Messages;

public class HolidayJson {
	private ObjectMapper mapper;
	private ArrayNode arrayNode;
	
	public HolidayJson() {
		mapper = new ObjectMapper();
		arrayNode = mapper.createArrayNode();
	}

	public List<Month> fromJson(ArrayNode json) {
		List<Month> months = new ArrayList<>();
		try {
			for (Iterator<JsonNode> it = json.iterator(); it.hasNext();)
				months.add(mapper.convertValue(it.next(), Month.class));
			return months;
		} catch (IllegalArgumentException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
		throw new NoSuchElementException(Messages.MSG_2.getMessage());
	}

	public JsonNode toJson(Integer day, String month, String event, List<Title> titles) {
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put(Key.MONTH.getKey(), month);
		ArrayNode jsonHolidays = objectNode.putArray(Key.HOLIDAYS.getKey());
		assignValues(day, event, titles, jsonHolidays);
		arrayNode.add(objectNode);
		return objectNode;
	}
	
	public ArrayNode toJson(String month, Integer day, ArrayNode arrayNode, String event, List<Title> titles) {
		List<Month> months = fromJson(arrayNode);
		ArrayNode jsonMonths = mapper.createArrayNode();
		for (Month m : months) {
			ObjectNode objectMonth = jsonMonths.objectNode();
			objectMonth.put(Key.MONTH.getKey(), m.getMonth());
			ArrayNode jsonHolidays = objectMonth.putArray(Key.HOLIDAYS.getKey());
			if(m.getMonth().equalsIgnoreCase(month))
				m.getHolidays().add(new Holiday(day, event, titles));
			for (Holiday h : m.getHolidays()) {
				assignValues(h.getDay(), h.getEvent(), h.getTitles(), jsonHolidays);
			}
			jsonMonths.add(objectMonth);
		}
		return jsonMonths;
	}

	private ObjectNode assignValues(Integer day, String event, List<Title> titles, ArrayNode jsonHolidays) {
		ObjectNode jsonHoliday = jsonHolidays.objectNode();
		jsonHoliday.put(Key.DAY.getKey(), day);
		jsonHoliday.put(Key.EVENT.getKey(), event);
		ArrayNode jsonTitles = jsonHoliday.putArray(Key.TITLES.getKey());
		putTitles(titles, jsonTitles);
		jsonHolidays.add(jsonHoliday);
		return jsonHoliday;
	}

//	Transforming json into string
	public String prettyPrintJsonString(JsonNode jsonNode) {
		try {
			Holiday json = mapper.readValue(jsonNode.toString(), Holiday.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		}
		throw new NoSuchElementException();
	}

	private void putTitles(List<Title> titles, ArrayNode jsonTitles) {
		for (Title t : titles) {
			ObjectNode jsonTitle = mapper.createObjectNode();
			jsonTitle.put(Key.NAME.getKey(), t.getName());
			ArrayNode jsonDescriptions = jsonTitle.putArray(Key.DESCRIPTIONS.getKey());
			putDescriptions(t, jsonDescriptions);
			jsonTitles.add(jsonTitle);
		}
	}

	private void putDescriptions(Title t, ArrayNode jsonDescriptions) {
		for (String d : t.getDescriptions()) {
			ObjectNode jsonDescription = mapper.createObjectNode();
			jsonDescription.put(Key.DESCRIPTION.getKey(), d);
			jsonDescriptions.add(jsonDescription);
		}
	}
}