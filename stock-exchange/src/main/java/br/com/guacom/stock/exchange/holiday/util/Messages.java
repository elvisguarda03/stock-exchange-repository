package br.com.guacom.stock.exchange.holiday.util;

public enum Messages {
	MSG_1("Os campos não podem estar vázios!"),
	MSG_2("Não existem elementos!"),
	MSG_3(""),
	MSG_4(""),
	MSG_5(""),
	MSG_6("");
	
	private String message;
	
	Messages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
