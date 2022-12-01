package com.petrhalik.converter.domain.converter;


import com.petrhalik.converter.controller.ConverterController;
import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.domain.converter.reader.JsonReader;
import com.petrhalik.converter.domain.converter.writer.CvsWriter;
import com.petrhalik.converter.testutils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonToCsvConverterTest {

    public static final String CSV_CONTENT_TYPE = "text/csv;charset=UTF-8";

    JsonToCsvConverter jsonToCsvConverter;
    @Before
    public void setup() {
        jsonToCsvConverter = new JsonToCsvConverter(new JsonReader(), new CvsWriter());
    }

    @Test
    public void accept() {
        assertTrue(jsonToCsvConverter.accept(InputType.json, OutputType.csv));
        assertFalse(jsonToCsvConverter.accept(null, OutputType.csv));
        assertFalse(jsonToCsvConverter.accept(InputType.json, null));
    }

    @Test
    public void convertArray() {
        String expectedDocument = "name,contact.mail,contact.phone\n" +
                "Pat,John@convotis.com,555-12345\n" +
                "Ann,Ann@convotis.com,555-434343\n";

        ConversionResult result = jsonToCsvConverter.convert(InputType.json, OutputType.csv, ConverterController.ARRAY_JSON);

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(result.getDocument()));
        assertEquals(CSV_CONTENT_TYPE, result.getContentType());
    }

    @Test
    public void convertArrayWithMissingElements() {
        String expectedDocument = "name,contact.town,contact.mail,contact.phone\n" +
                "Pat,Bratislava,John@convotis.com,555-12345\n" +
                "Ann,,Ann@convotis.com,555-434343\n" +
                "Tom,Plzeň,Tom@convotis.com,\n";

        ConversionResult result = jsonToCsvConverter.convert(InputType.json, OutputType.csv, ConverterController.ARRAY_JSON_WITH_MISSING_ELEMENTS);

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(result.getDocument()));
        assertEquals(CSV_CONTENT_TYPE, result.getContentType());
    }

}