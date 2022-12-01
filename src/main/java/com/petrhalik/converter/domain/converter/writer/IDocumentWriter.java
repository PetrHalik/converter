package com.petrhalik.converter.domain.converter.writer;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.DocumentData;

/**
 * Interface for Writers to write an internal structure as stream in required format
 */
public interface IDocumentWriter {

    ConversionResult createDocument(DocumentData documentData);

}
