package com.petrhalik.converter.domain.converter.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Document for output
 */
@Getter
@RequiredArgsConstructor
public class ConversionResult {

    private final String document;
    private final String filename;
    private final String contentType;
}
