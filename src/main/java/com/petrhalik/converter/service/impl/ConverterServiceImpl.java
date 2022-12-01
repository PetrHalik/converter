package com.petrhalik.converter.service.impl;

import com.petrhalik.converter.domain.converter.*;
import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.exception.NotSupportedException;
import com.petrhalik.converter.service.IConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConverterServiceImpl implements IConverterService {

    private final ConverterFactory converterFactory;

    @Override
    public ConversionResult convertDocument(InputType inputFormat, OutputType outputFormat, String documentToConvert) {

        Optional<IConverter> converterOptional = converterFactory.getConverter(inputFormat, outputFormat);

        if (converterOptional.isEmpty()) {
            throw new NotSupportedException("Conversion from " + inputFormat + " to " + outputFormat + " is not supported.");
        }

        return converterOptional.get().convert(inputFormat, outputFormat,documentToConvert);
    }

}
