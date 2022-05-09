import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PayloadTest {

	@Test
	public void getPayload() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String data = "{\n" + "  \"schema\": {\n" + "    \"name\": \"dbserver1.inventory.customers.Envelope\",\n"
				+ "    \"optional\": false,\n" + "    \"type\": \"struct\",\n" + "    \"fields\": [\n" + "      {\n"
				+ "        \"field\": \"before\",\n" + "        \"name\": \"dbserver1.inventory.customers.Value\",\n"
				+ "        \"optional\": true,\n" + "        \"type\": \"struct\",\n" + "        \"fields\": [\n"
				+ "          {\n" + "            \"field\": \"id\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int32\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"first_name\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"last_name\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"email\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          }\n" + "        ]\n" + "      },\n" + "      {\n"
				+ "        \"field\": \"after\",\n" + "        \"name\": \"dbserver1.inventory.customers.Value\",\n"
				+ "        \"optional\": true,\n" + "        \"type\": \"struct\",\n" + "        \"fields\": [\n"
				+ "          {\n" + "            \"field\": \"id\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int32\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"first_name\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"last_name\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"email\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          }\n" + "        ]\n" + "      },\n" + "      {\n"
				+ "        \"field\": \"source\",\n" + "        \"name\": \"io.debezium.connector.mysql.Source\",\n"
				+ "        \"optional\": false,\n" + "        \"type\": \"struct\",\n" + "        \"fields\": [\n"
				+ "          {\n" + "            \"field\": \"version\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"connector\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"name\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"server_id\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int64\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"ts_sec\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int64\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"gtid\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"file\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"pos\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int64\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"row\",\n" + "            \"optional\": false,\n"
				+ "            \"type\": \"int32\"\n" + "          },\n" + "          {\n"
				+ "            \"default\": false,\n" + "            \"field\": \"snapshot\",\n"
				+ "            \"optional\": true,\n" + "            \"type\": \"boolean\"\n" + "          },\n"
				+ "          {\n" + "            \"field\": \"thread\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"int64\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"db\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"table\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          },\n" + "          {\n"
				+ "            \"field\": \"query\",\n" + "            \"optional\": true,\n"
				+ "            \"type\": \"string\"\n" + "          }\n" + "        ]\n" + "      },\n" + "      {\n"
				+ "        \"field\": \"op\",\n" + "        \"optional\": false,\n" + "        \"type\": \"string\"\n"
				+ "      },\n" + "      {\n" + "        \"field\": \"ts_ms\",\n" + "        \"optional\": true,\n"
				+ "        \"type\": \"int64\"\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"payload\": {\n"
				+ "    \"op\": \"c\",\n" + "    \"before\": null,\n" + "    \"after\": {\n"
				+ "      \"last_name\": \"Thomas\",\n" + "      \"id\": 1001,\n" + "      \"first_name\": \"Sally\",\n"
				+ "      \"email\": \"sally.thomas@acme.com\"\n" + "    },\n" + "    \"source\": {\n"
				+ "      \"ts_sec\": 0,\n" + "      \"query\": null,\n" + "      \"thread\": null,\n"
				+ "      \"server_id\": 0,\n" + "      \"version\": \"0.9.5.Final\",\n"
				+ "      \"file\": \"mysql-bin.000003\",\n" + "      \"connector\": \"mysql\",\n"
				+ "      \"pos\": 154,\n" + "      \"name\": \"dbserver1\",\n" + "      \"gtid\": null,\n"
				+ "      \"row\": 0,\n" + "      \"snapshot\": true,\n" + "      \"db\": \"inventory\",\n"
				+ "      \"table\": \"customers\"\n" + "    },\n" + "    \"ts_ms\": 1641295367362\n" + "  }\n" + "}";
		
			JsonNode node = mapper.readTree(data.getBytes());
			
			boolean test= node.has("/payload");
			JsonNode payload = node.at("/payload");

			assertNotNull(payload);
	
	}

}
