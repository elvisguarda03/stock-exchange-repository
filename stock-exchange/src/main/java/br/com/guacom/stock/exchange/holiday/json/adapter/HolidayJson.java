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
import br.com.guacom.stock.exchange.holiday.models.Title;
import br.com.guacom.stock.exchange.holiday.util.Key;
import br.com.guacom.stock.exchange.holiday.util.Messages;

public class HolidayJson {
	private ObjectMapper mapper;

	public HolidayJson() {
		mapper = new ObjectMapper();
	}

	public List<Holiday> fromJson(ArrayNode json) {
		List<Holiday> holidays = new ArrayList<>();
		try {
			for (Iterator<JsonNode> it = json.iterator(); it.hasNext();) {
				holidays.add(mapper.convertValue(it.next(), Holiday.class));
			}
			return holidays;
		} catch (IllegalArgumentException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
		throw new NoSuchElementException(Messages.MSG_2.getMessage());
	}

	public JsonNode toJson(Integer day, String month, String event, List<Title> titles) {
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put(Key.DAY.getKey(), day);
		objectNode.put(Key.MONTH.getKey(), month);
		objectNode.put(Key.EVENT.getKey(), event);
		ArrayNode jsonTitles = objectNode.putArray(Key.TITLES.getKey());
		putTitles(titles, jsonTitles);
		return objectNode;
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