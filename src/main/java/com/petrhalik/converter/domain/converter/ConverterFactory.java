package com.petrhalik.converter.domain.converter;

import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * Factory to provide converters for required input and output formats
 */
@Component
public class ConverterFactory {

    private final Set<IConverter> convertersSet;

    public ConverterFactory(Set<IConverter> convertersSet) {
        this.convertersSet = convertersSet;
    }

    /**
     * Method returns a converter by input and output format
     * @param inputType input format type
     * @param outputType output format type
     * @return converter or empty
     */
    public Optional<IConverter> getConverter(InputType inputType, OutputType outputType) {
        return convertersSet.stream()
                .filter(converter -> converter.accept(inputType, outputType))
                .findFirst();
    }

}
