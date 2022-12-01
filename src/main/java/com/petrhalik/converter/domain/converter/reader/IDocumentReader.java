package com.petrhalik.converter.domain.converter.reader;

import com.petrhalik.converter.domain.converter.model.DocumentData;

public interface IDocumentReader {

    DocumentData readDocument(String document);
}
