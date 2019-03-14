package br.com.guacom.stock.exchange.holiday.util;

public enum Key {
	DAY("dia"),
	MONTH("mes"),
	TITLES("titulos"),
	EVENT("evento"),
	NAME("nome"),
	DESCRIPTION("descricao"),
	DESCRIPTIONS("descricoes"),

	//	Patterns to break list of descriptions
	EXPRESSION_REGULAR_1(";"),
	EXPRESSION_REGULAR_2("\\.");
	
	private String key;
	
	Key(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}