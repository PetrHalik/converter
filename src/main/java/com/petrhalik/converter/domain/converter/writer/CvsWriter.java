package com.petrhalik.converter.domain.converter.writer;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.DocumentData;
import com.petrhalik.converter.exception.ConversionException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to wtite an internal structure as a CSV string
 */
@Component
public class CvsWriter implements IDocumentWriter {

    public static final String RESULT_FILE_NAME = "output_";
    public static final String CONTENT_TYPE = "text/csv;charset=UTF-8";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

    public ConversionResult createDocument(DocumentData documentData)  {
        String[] headers = documentData.getHeadersSet().toArray(new String[0]);
        StringWriter out = new StringWriter();

        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL
                .withHeader(headers))) {
            for(Map<String, String> row : documentData.getRows()) {
                List<String> fullRow = documentData.getHeadersSet().stream()
                        .map(row::get)
                        .collect(Collectors.toList());
                printer.printRecord(fullRow);
            }
        } catch (Exception e) {
            throw new ConversionException("Error by building a csv file.", e);
        }
        return new ConversionResult(out.toString(), RESULT_FILE_NAME + dateFormat.format(new Date()), CONTENT_TYPE);
    }
}
