package br.com.guacom.stock.exchange.holiday.util;

public enum Key {
//	Nomenclature of JSON keys
	DAY("dia"),
	MONTH("mes"),
	HOLIDAYS("feriados"),
	TITLES("titulos"),
	EVENT("evento"),
	NAME("nome"),
	DESCRIPTION("descricao"),
	DESCRIPTIONS("descricoes");

	private String key;
	
	Key(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}