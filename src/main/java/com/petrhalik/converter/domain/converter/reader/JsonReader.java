package com.petrhalik.converter.domain.converter.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.petrhalik.converter.domain.converter.model.DocumentData;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class to convert a json document to an internal structure
 */
@Component
public class JsonReader implements IDocumentReader{

    public DocumentData readDocument(String document) {
        try {
            List<Map<String, String>> rows = new ArrayList<>();
            Set<String> headers = new LinkedHashSet<>();
            read(new ObjectMapper().readTree(document), rows, headers);
            return new DocumentData(headers, rows);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("Json document is malformed.", e);
        }
    }

    private void read(JsonNode root, List<Map<String, String>> valuesMap, Set<String> headers) {
        if(root.isArray()) {
            // json is an array
            root.elements().forEachRemaining(it -> {
                Map<String, String> map = new HashMap<>();
                valuesMap.add(map);
                addToHeader( "", it    , map, headers);
            });
        } else {
            // root is an object
            root.fields().forEachRemaining(it -> {
                Map<String, String> map = new HashMap<>();
                valuesMap.add(map);
                // check for a plain json or a single object
                // single object - no need to have "menu" in header - {"menu": { "id": "file",  }}
                // plain json will have every key in menu - {"menu1": "value1", "menu2": "value2"}
                String startHeader = root.isObject() ? it.getKey(): "";
                addToHeader( startHeader, it.getValue()    , map, headers);
            });
        }
    }

    private void addToHeader(String currentHeader, JsonNode jsonNode, Map<String, String> map, Set<String> headers) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentHeader.isEmpty() ? "" : currentHeader + ".";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addToHeader(pathPrefix + entry.getKey(), entry.getValue(), map, headers);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addToHeader(currentHeader, arrayNode.get(i), map, headers);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            headers.add(currentHeader);
            map.put(currentHeader, valueNode.asText());
        }
    }
}
