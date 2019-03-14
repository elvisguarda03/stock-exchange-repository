package br.com.guacom.stock.exchange.holiday;

import java.util.List;

import br.com.guacom.stock.exchange.holiday.json.adapter.HolidayJson;
import br.com.guacom.stock.exchange.holiday.models.CalendarScraping;
import br.com.guacom.stock.exchange.holiday.models.Holiday;

public class Main {

	public static void main(String[] args) {
		CalendarScraping cs = new CalendarScraping();
		List<Holiday> holidays = null;
		cs.buildJsonThroughTheDataInTheTag();
		HolidayJson adapter = new HolidayJson();
		holidays = adapter.fromJson(cs.getJsonNode());
		for (Holiday h : holidays)
			System.out.println(h);
	}
}
