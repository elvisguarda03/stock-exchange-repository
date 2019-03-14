package br.com.guacom.stock.exchange.holiday.json.adapter;

import java.io.IOException;
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
	private ArrayNode arrayNode;
	
	public HolidayJson() {
		mapper = new ObjectMapper();
	}

	public Holiday fromJson(JsonNode json) {
		try {
			Holiday holiday = mapper.convertValue(json, Holiday.class);
			return holiday;
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
		configureTitles(titles, jsonTitles);
		arrayNode.add(objectNode);
		return arrayNode;
	}

//	Transformando o json em string
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

	private void configureTitles(List<Title> titles, ArrayNode jsonTitles) {
		for (Title t : titles) {
			ObjectNode jsonTitle = mapper.createObjectNode();
			jsonTitle.put(Key.NAME.getKey(), t.getName());
			ArrayNode jsonDescriptions = jsonTitle.putArray(Key.DESCRIPTIONS.getKey());
			for (String d : t.getDescriptions()) {
				ObjectNode jsonDescription = mapper.createObjectNode();
				jsonDescription.put(Key.DESCRIPTION.getKey(), d);
				jsonDescriptions.add(jsonDescription);
			}
			jsonTitles.add(jsonTitle);
		}
	}
}