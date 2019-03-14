package br.com.guacom.stock.exchange.holiday.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DescriptionDeserializer extends StdDeserializer<List<String>> {

	private static final long serialVersionUID = 1L;

	public DescriptionDeserializer(Class<?> vc) {
		super(vc);
	}

	public DescriptionDeserializer() {
		this(null);
	}

	@Override
	public List<String> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode jsonNode = jp.getCodec().readTree(jp);
		List<String> descricoes = jsonNode.findValuesAsText("descricao");
		return descricoes;
	}
}