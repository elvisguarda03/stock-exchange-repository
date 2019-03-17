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
import br.com.guacom.stock.exchange.holiday.util.Pattern;

public class CalendarScraping {
	private final String URL = "http://www.b3.com.br/pt_br/solucoes/plataformas/puma-trading-system/para-participantes-e-traders/calendario-de-negociacao/feriados/#panel1a";
	private Document doc;
	private ArrayNode arrayNode;
	private HolidayJson adapterForJson;
	private Elements elements;
	private List<Title> titles;
	private List<String> descriptionData;

	public CalendarScraping() {
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

//	Generating json.
	public void buildJsonThroughTheDataInTheTag() throws Exception {
		elements = getListElementsByClass();
		for (int i = 0; i < 11; i++) {
			verifyAmountOfHolidaysInTheMonth(i);
		}
	}

	private Elements getListContentsByTag(int index) {
		return elements.get(index).getElementsByTag("tbody").first().getElementsByTag("tr");
	}

	private void verifyAmountOfHolidaysInTheMonth(int index) {
		String monthPrevious = null;
		Elements holidays = getListContentsByTag(index);
		Month month = new Month(getMonthOfTag(elements, index));
		monthPrevious = month.getMonth();
		Integer day = null;
		String event = null;
		if (holidays.size() == 1) {
			day = getDayOfTag(holidays, 0);
			event = getEventOfTag(holidays, 0);
			getTextsOfTag(holidays);
			toJson(day, month, event, titles);
		} else {
			for (int i = 0; i < holidays.size(); i++) {
				day = getDayOfTag(holidays, i);
				event = getEventOfTag(holidays, i);
				getTextsOfTag(holidays.get(i));
				if (i > 0 && monthPrevious.equalsIgnoreCase(month.getMonth())) {
					toJson(monthPrevious, day, event, titles);
					continue;
				}
				toJson(day, month, event, titles);
			}
		}
	}

//	private void assignValues(Elements elementsOfTag, Integer day, String event, int index) {
//		day = getDayOfTag(elementsOfTag, index);
//		event = getEventOfTag(elementsOfTag, index);
//	}

	private void toJson(String month, Integer day, String event, List<Title> titles) {
		ArrayNode json = adapterForJson.toJson(month, day, arrayNode, event, titles);
		arrayNode.removeAll();
		arrayNode.addAll(json);
	}

	private Elements getListElementsByClass() {
		return doc.getElementsByClass("bg-conteudo").first().getElementsByClass("accordion").first()
				.getElementsByClass("accordion-navigation");
	}

//	Will only be executed if there is only one month holiday.
	private void getTextsOfTag(Elements elements) {
		getTextsOfTag(elements.first());
	}

//	Will only be executed if there is more than one month holiday.
	private void getTextsOfTag(Element element) {
		Elements elementsByTag = getAllTagTd(element);
		titles = new ArrayList<>();
		for (int j = 0; j < elementsByTag.size(); j++) {
			if (j > 1 && elementsByTag.get(j).hasText()) {
				associateDescriptionWithTitle(elementsByTag.get(j));
				continue;
			}
		}
	}

	private Elements getAllTagTd(Element element) {
		return element.getElementsByTag("td");
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
		for (int i = 0; i < descriptionsElements.size(); i++) {
			StringBuilder builder = new StringBuilder();
			appendDescription(descriptionsElements.get(i), builder);
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

	private StringBuilder appendDescription(Element descriptionsElement, StringBuilder builder) {
		builder.append(descriptionsElement.text().trim());
		return builder;
	}

	public void toJson(Integer day, Month month, String event, List<Title> titles) {
		adapterForJson = new HolidayJson();
		JsonNode jsonNode = adapterForJson.toJson(day, month.getMonth(), event, titles);
		arrayNode.add(jsonNode);
	}

//	Looking for a pattern to pick up the descriptions separately. All descriptions have this pattern.
	private void findForPattern(String data) {
		if (data.contains(Pattern.EXPRESSION_REGULAR_1.getPattern())
				|| data.contains(Pattern.EXPRESSION_REGULAR_2.getPattern())) {
			splitBasedPattern(data);
		} else {
			descriptionData = new ArrayList<>();
			descriptionData.add(data);
		}
	}

//	Applying the break pattern to capture the descriptions separately.
	private void splitBasedPattern(String data) {
		descriptionData = new ArrayList<>();
		String[] offset = data.split(Pattern.EXPRESSION_REGULAR_1.getPattern());
		for (int i = 0; i < offset.length; i++) {
			if (offset[i].contains(Pattern.EXPRESSION_REGULAR_2.getPattern())) {
				String[] descriptions = offset[i].split(Pattern.EXPRESSION_REGULAR_2.getPattern());
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