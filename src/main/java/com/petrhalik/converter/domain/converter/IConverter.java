package com.petrhalik.converter.domain.converter;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import org.springframework.http.converter.HttpMessageNotReadableException;

public interface IConverter {

    boolean accept(InputType inputType, OutputType outputType);

    ConversionResult convert(InputType inputFormat, OutputType outputFormat, String inputDocument) throws HttpMessageNotReadableException;
}
