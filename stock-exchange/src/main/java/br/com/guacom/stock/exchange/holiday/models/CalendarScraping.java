package br.com.guacom.stock.exchange.holiday.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.guacom.stock.exchange.holiday.json.adapter.HolidayJson;
import br.com.guacom.stock.exchange.holiday.util.Key;

public class CalendarScraping {
	private final String URL = "http://www.b3.com.br/pt_br/solucoes/plataformas/puma-trading-system/para-participantes-e-traders/calendario-de-negociacao/feriados/#panel1a";
	private Document doc;
	private JsonNode jsonNode;
	private Elements elements;
	private List<Title> titles;
	private List<String> descriptionData;

	public CalendarScraping() {
		titles = new ArrayList<>();
		openConnection();
	}

	public void openConnection() {
		try {
			doc = Jsoup.connect(URL).timeout(10 * 1000).get();
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

//	Taking the data necessary and generating json
	public void buildJsonThroughTag(int index) {
		try {
			elements = doc.getElementsByClass("bg-conteudo").first().getElementsByClass("accordion").first()
					.getElementsByClass("accordion-navigation");
//			for (int i = 0; i < elements.size(); i++) {
				String month = getMonthOfTag(elements, index);
				Elements elementsOfTag = elements.get(index).getElementsByTag("tbody").first().getElementsByTag("tr");
//				if (elementsOfTag.size() == 1) {
					Integer day = getDayOfTag(elementsOfTag);
					String event = getEventOfTag(elementsOfTag);
					getTextsOfTag(elementsOfTag);
					toJson(day, month, event, titles);
//				}
//				else {
					
//				}
//			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	private String getEventOfTag(Elements elements) {
		String event;
		event = elements.first().getElementsByTag("td").get(1).text();
		return event;
	}

	private String getMonthOfTag(Elements elements, int index) {
		return elements.get(index).getElementsByTag("a").first().text();
	}

	private Integer getDayOfTag(Elements elements) {
		return Integer.parseInt(elements.first().getElementsByTag("td").first().text());
	}

//	Pega todas as tags td e checa se dentro da tag possui texto
//	Get all tags td and check if inside the tag has text
	private void getTextsOfTag(Elements elements) {
		for (int i = 0; i < elements.size(); i++) {
			Elements elementsByTag = elements.get(i).getElementsByTag("td");
			for (int j = 0; j < elementsByTag.size(); j++) {
				if (j > 1 && elementsByTag.get(j).hasText()) {
					associateDescriptionWithTitle(elementsByTag.get(j));
					continue;
				}
			}
		}
	}

//	Pega todas as descrições e associa ao seu respectivo título
//	It takes all the descriptions and associates to its respective title
	private void associateDescriptionWithTitle(Element element) {
		StringBuilder builder = new StringBuilder();
		Elements descriptionsElements = element.getElementsByTag("ul");
		Elements titleElement = element.getElementsByTag("p");
		for (int i = 0; i < descriptionsElements.size(); i++) {
			builder.append(descriptionsElements.get(i).text().trim());
			findForPattern(builder.toString());
			if (titleElement.size() > i) {
				titles.add(new Title(titleElement.get(i).text(), descriptionData));
				continue;
			}
			titles.add(new Title(descriptionData));
		}
	}

	public void toJson(Integer day, String month, String event, List<Title> titles) {
		HolidayJson adapterForJson = new HolidayJson();
		jsonNode = adapterForJson.toJson(day, month, event, titles);
	}

//	Procurando um padrão para pegar as descrições separadamente. Todas as descrições possuem este padrão
//	Looking for a pattern to pick up the descriptions separately. All descriptions have this pattern
	private void findForPattern(String data) {
		if (data.contains(";") || data.contains(".")) {
			applyPattern(data);
		}
	}

//	Aplicando o padrão de quebra para capturar as descrições separadamente.
//	Applying the break pattern to capture the descriptions separately.
	private void applyPattern(String data) {
		descriptionData = new ArrayList<>();
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

	public JsonNode getJsonNode() {
		return jsonNode;
	}
}