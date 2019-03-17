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

import br.com.guacom.stock.exchange.holiday.models.Feriado;
import br.com.guacom.stock.exchange.holiday.models.Mes;
import br.com.guacom.stock.exchange.holiday.models.Titulo;
import br.com.guacom.stock.exchange.holiday.util.Key;
import br.com.guacom.stock.exchange.holiday.util.Messages;

public class HolidayJson {
	private ObjectMapper mapper;
	private ArrayNode arrayNode;

	public HolidayJson() {
		mapper = new ObjectMapper();
		arrayNode = mapper.createArrayNode();
	}

	public List<Mes> fromJson(ArrayNode json) {
		List<Mes> months = new ArrayList<>();
		try {
			for (Iterator<JsonNode> it = json.iterator(); it.hasNext();)
				months.add(mapper.convertValue(it.next(), Mes.class));
			return months;
		} catch (IllegalArgumentException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
		throw new NoSuchElementException(Messages.MSG_2.getMessage());
	}

//	Creating a new json of month associating with holidays
	public JsonNode toJson(Integer day, String month, String event, List<Titulo> titles) {
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put(Key.MONTH.getKey(), month);
		ArrayNode jsonHolidays = objectNode.putArray(Key.HOLIDAYS.getKey());
		assignValues(day, event, titles, jsonHolidays);
		arrayNode.add(objectNode);
		return objectNode;
	}

//	Creating new holidays for month
	public ArrayNode toJson(String month, Integer day, ArrayNode arrayNode, String event, List<Titulo> titles) {
		List<Mes> months = fromJson(arrayNode);
		ArrayNode jsonMonths = mapper.createArrayNode();
		for (Mes m : months) {
			ObjectNode objectMonth = newMonth(month, day, event, titles, jsonMonths, m);
			jsonMonths.add(objectMonth);
		}
		return jsonMonths;
	}

	private ObjectNode newMonth(String month, Integer day, String event, List<Titulo> titles, ArrayNode jsonMonths,
			Mes m) {
		ObjectNode objectMonth = jsonMonths.objectNode();
		objectMonth.put(Key.MONTH.getKey(), m.getMes());
		ArrayNode jsonHolidays = objectMonth.putArray(Key.HOLIDAYS.getKey());
		if (m.getMes().equalsIgnoreCase(month))
			m.getFeriados().add(new Feriado(day, event, titles));
		for (Feriado h : m.getFeriados()) {
			assignValues(h.getDia(), h.getEvento(), h.getTitulos(), jsonHolidays);
		}
		return objectMonth;
	}

	private ObjectNode assignValues(Integer day, String event, List<Titulo> titles, ArrayNode jsonHolidays) {
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
			Mes json = mapper.readValue(jsonNode.toString(), Mes.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		}
		throw new NoSuchElementException();
	}

	private void putTitles(List<Titulo> titles, ArrayNode jsonTitles) {
		for (Titulo t : titles) {
			ObjectNode jsonTitle = mapper.createObjectNode();
			jsonTitle.put(Key.NAME.getKey(), t.getNome());
			ArrayNode jsonDescriptions = jsonTitle.putArray(Key.DESCRIPTIONS.getKey());
			putDescriptions(t, jsonDescriptions);
			jsonTitles.add(jsonTitle);
		}
	}

	private void putDescriptions(Titulo t, ArrayNode jsonDescriptions) {
		for (String d : t.getDescricoes()) {
			ObjectNode jsonDescription = mapper.createObjectNode();
			jsonDescription.put(Key.DESCRIPTION.getKey(), d);
			jsonDescriptions.add(jsonDescription);
		}
	}
}