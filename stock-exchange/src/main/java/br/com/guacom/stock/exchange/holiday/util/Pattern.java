package br.com.guacom.stock.exchange.holiday.util;

public enum Pattern {
//	Patterns to break list of descriptions
	EXPRESSION_REGULAR_1(";"),
	EXPRESSION_REGULAR_2("\\.");

	private String pattern;

	Pattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
}
