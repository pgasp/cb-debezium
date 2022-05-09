package com.couchbase.connect.kafka.debezium;

import com.couchbase.connect.kafka.handler.sink.SinkAction;
import com.couchbase.connect.kafka.handler.sink.SinkDocument;
import com.couchbase.connect.kafka.handler.sink.SinkHandler;
import com.couchbase.connect.kafka.handler.sink.SinkHandlerContext;
import com.couchbase.connect.kafka.handler.sink.SinkHandlerParams;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.common.config.ConfigException;

import java.io.IOException;

/**
 * An example sink handler that reads a field name from the
 * {@code "example.field.to.remove"} connector config property and removes that
 * field from every message before writing the document to Couchbase.
 */
public class DebeziumSinkHandler implements SinkHandler {
	private static final ObjectMapper mapper = new ObjectMapper();

	public SinkAction handle(SinkHandlerParams params) {
		String documentId = getDocumentId(params);
		SinkDocument doc = params.document().orElse(null);
		return doc == null ? SinkAction.ignore() // ignore Kafka records with null values
				: SinkAction.upsertJson(params, params.collection(), documentId, transform(doc.content()));
	}

	private byte[] transform(byte[] content) {
		try {
			JsonNode node = mapper.readTree(content);

			

				JsonNode payload = node.at("/payload");
				return mapper.writeValueAsBytes(payload);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}