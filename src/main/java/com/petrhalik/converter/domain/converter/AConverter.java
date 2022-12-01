package com.petrhalik.converter.domain.converter;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.DocumentData;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.domain.converter.reader.IDocumentReader;
import com.petrhalik.converter.domain.converter.writer.IDocumentWriter;
import com.petrhalik.converter.exception.NotSupportedException;
import lombok.RequiredArgsConstructor;

/**
 * An abstract class for converting of document
 */
@RequiredArgsConstructor
public abstract class AConverter implements IConverter {

    private final IDocumentReader reader;
    private final IDocumentWriter writer;

    public abstract boolean accept(InputType inputType, OutputType outputType);

    /**
     * Base method for a conversion. It uses a reader to convert a document to the internal structure and
     * a writer to prepare output format
     *
     * @param inputFormat   input format type
     * @param outputFormat  output format type
     * @param inputDocument document to be converted
     * @return Result of conversion
     */
    @Override
    public ConversionResult convert(InputType inputFormat, OutputType outputFormat, String inputDocument) {
        if (!accept(inputFormat, outputFormat)) {
            throw new NotSupportedException("Conversion from " + inputFormat + " to " + outputFormat + " is not supported.");
        }
        DocumentData documentData = reader.readDocument(inputDocument);
        return writer.createDocument(documentData);
    }
}
