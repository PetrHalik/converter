package com.petrhalik.converter.service;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;

public interface IConverterService {

    ConversionResult convertDocument(InputType inputFormat, OutputType outputFormat, String valueToConvert) ;
}
