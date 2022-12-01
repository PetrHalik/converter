package com.petrhalik.converter.domain.converter;

import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.domain.converter.reader.JsonReader;
import com.petrhalik.converter.domain.converter.writer.CvsWriter;
import org.springframework.stereotype.Component;

/**
 * Method converts a json document to a flat internal structure
 */
@Component
public class JsonToCsvConverter extends AConverter implements IConverter {

    public static final InputType JSON_INPUT_TYPE = InputType.json;
    public static final OutputType CSV_OUTPUT_TYPE = OutputType.csv;

    public JsonToCsvConverter(JsonReader reader, CvsWriter writer) {
        super(reader, writer);
    }

    @Override
    public boolean accept(InputType inputType, OutputType outputType) {
        return inputType == JSON_INPUT_TYPE && outputType == CSV_OUTPUT_TYPE;
    }

}
