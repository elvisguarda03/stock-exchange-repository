package br.com.guacom.stock.exchange.holiday.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import br.com.guacom.stock.exchange.holiday.json.adapter.HolidayJson;
import br.com.guacom.stock.exchange.holiday.util.Key;

public class CalendarScraping {
	private final String URL = "http://www.b3.com.br/pt_br/solucoes/plataformas/puma-trading-system/para-participantes-e-traders/calendario-de-negociacao/feriados/#panel1a";
	private Document doc;
	private JsonNode jsonNode;
	private ArrayNode arrayNode;
	private Elements elements;
	private List<Title> titles;
	private List<String> descriptionData;

	public CalendarScraping() {
		titles = new ArrayList<>();
		descriptionData = new ArrayList<>();
		arrayNode = new ObjectMapper().createArrayNode();
		openConnection();
	}

	public void openConnection() {
		try {
			doc = Jsoup.connect(URL).timeout(10 * 1000).get();
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

//	Taking the data necessary and generating json.
	public void buildJsonThroughTheDataInTheTag() {
		try {
			elements = getListElementsByClass();
			for (int i = 0; i < elements.size(); i++) {
				String month = getMonthOfTag(elements, i);
				Elements elementsOfTag = getListContentsByTag(i);
				Integer day = null;
				String event = null;
				assignValues(elementsOfTag, day, event);
				toJson(day, month, event, titles);
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	private Elements getListContentsByTag(int index) {
		return elements.get(index).getElementsByTag("tbody").first().getElementsByTag("tr");
	}

	private void assignValues(Elements elementsOfTag, Integer day, String event) {
		if (elementsOfTag.size() == 1) {
			day = getDayOfTag(elementsOfTag, 0);
			event = getEventOfTag(elementsOfTag, 0);
			getTextsOfTag(elementsOfTag);
		} else {
			for (int j = 0; j < elementsOfTag.size(); j++) {
				day = getDayOfTag(elementsOfTag, j);
				event = getEventOfTag(elementsOfTag, j);
				getTextsOfTag(elementsOfTag.get(j));
			}
		}
	}

	private Elements getListElementsByClass() {
		return doc.getElementsByClass("bg-conteudo").first().getElementsByClass("accordion").first()
				.getElementsByClass("accordion-navigation");
	}

//	Get all tags td and check if inside the tag has text.
//	Will only be executed if there is only one month holiday.
	private void getTextsOfTag(Elements elements) {
		for (int i = 0; i < elements.size(); i++) {
			getTextsOfTag(elements.get(i));
		}
	}

//	Get all tags td and check if inside the tag has text. 
//	Will only be executed if there is more than one month holiday.
	private void getTextsOfTag(Element element) {
		Elements elementsByTag = element.getElementsByTag("td");
		titles.clear();
		for (int j = 0; j < elementsByTag.size(); j++) {
			if (j > 1 && elementsByTag.get(j).hasText()) {
				associateDescriptionWithTitle(elementsByTag.get(j));
				continue;
			}
		}
	}

	private String getEventOfTag(Elements elements, int index) {
		return elements.get(index).getElementsByTag("td").get(1).text();
	}

	private String getMonthOfTag(Elements elements, int index) {
		return elements.get(index).getElementsByTag("a").first().text();
	}

	private Integer getDayOfTag(Elements elements, int index) {
		return Integer.parseInt(elements.get(index).getElementsByTag("td").first().text());
	}

//	Associates the descriptions to their respective title.
	private void associateDescriptionWithTitle(Element element) {
		Elements descriptionsElements = getListDescriptions(element);
		Elements titleElement = getTagTitle(element);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < descriptionsElements.size(); i++) {
			appendDescription(descriptionsElements, builder, i);
			findForPattern(builder.toString());
			checkIfExistsTitle(titleElement, i);
		}
	}

	private void checkIfExistsTitle(Elements titleElement, int amount) {
		if (titleElement.size() > amount) {
			titles.add(new Title(titleElement.get(amount).text(), descriptionData));
			return;
		}
		titles.add(new Title(descriptionData));
	}

	private Elements getTagTitle(Element element) {
		return element.getElementsByTag("p");
	}

	private Elements getListDescriptions(Element element) {
		return element.getElementsByTag("ul");
	}

	private StringBuilder appendDescription(Elements descriptionsElements, StringBuilder builder, int i) {
		builder.append(descriptionsElements.get(i).text().trim());
		return builder;
	}

	public void toJson(Integer day, String month, String event, List<Title> titles) {
		HolidayJson adapterForJson = new HolidayJson();
		jsonNode = adapterForJson.toJson(day, month, event, titles);
		arrayNode.add(jsonNode);
	}

//	Looking for a pattern to pick up the descriptions separately. All descriptions have this pattern.
	private void findForPattern(String data) {
		if (data.contains(Key.EXPRESSION_REGULAR_1.getKey()) || data.contains(Key.EXPRESSION_REGULAR_2.getKey())) {
			splitBasedPattern(data);
		}
	}

//	Applying the break pattern to capture the descriptions separately.
	private void splitBasedPattern(String data) {
		descriptionData.clear();
		String[] offset = data.split(Key.EXPRESSION_REGULAR_1.getKey());
		for (int i = 0; i < offset.length; i++) {
			if (offset[i].contains(Key.EXPRESSION_REGULAR_2.getKey())) {
				String[] descriptions = offset[i].split(Key.EXPRESSION_REGULAR_2.getKey());
				descriptionData.addAll(List.of(descriptions));
				continue;
			}
			descriptionData.add(offset[i]);
		}
	}

	public ArrayNode getJsonNode() {
		return arrayNode;
	}
}