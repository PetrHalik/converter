package com.petrhalik.converter.controller;


import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.exception.ConversionException;
import com.petrhalik.converter.exception.NotSupportedException;
import com.petrhalik.converter.service.IConverterService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.EnumUtils.getEnumIgnoreCase;

@Slf4j
@RestController
@RequestMapping(value = "/converter")
@Api(value = "Converter Related APIs")
@ResponseBody
public class ConverterController {

    private final IConverterService converterService;

    public ConverterController(IConverterService converterService) {
        this.converterService = converterService;
    }

    @PostMapping(value = "/{inputFormat}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Convert document to required format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Document Converted Successfully"),
            @ApiResponse(code = 400, message = "Body of a request is not json or json is malformed"),
            @ApiResponse(code = 406, message = "Required output or input format is not supported"),
    })
    void convertDocument(@ApiParam(name = "document", value = bodyDescription) @RequestBody String document,
                         @ApiParam(name = "inputFormat", value = "Input format (only json is supported yet)", example = "json") @PathVariable final String inputFormat,
                         @ApiParam(name = "outputFormat", value = "Output format (only csv is supported yet)", example = "csv") @RequestParam(name = "outputFormat") String outputFormat,
                         HttpServletResponse response
    )  {
        InputType inputFormatEnum = getEnumIgnoreCase(InputType.class, inputFormat);
        OutputType requestedOutputFormat = getEnumIgnoreCase(OutputType.class, outputFormat);

        if (inputFormatEnum == null || requestedOutputFormat == null ) {
            String message = "Conversion from " + inputFormat + " to " + outputFormat + " is not supported.";
            log.error(message);
            throw new NotSupportedException(message);
        }

        ConversionResult result = converterService.convertDocument(inputFormatEnum, requestedOutputFormat, document);
        prepareResponse(result, response);
    }

    private void prepareResponse(ConversionResult result, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(result.getContentType());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", result.getFilename());
        response.setHeader(headerKey, headerValue);
        try {
            response.getWriter().print(result.getDocument());
        } catch (Exception e) {
            throw new ConversionException("Can't write to response",e);
        }
    }

    public static final String ARRAY_JSON = "[\n" +
            "{\"name\": \"Pat\",\"contact\": {\"mail\": \"John@convotis.com\",\"phone\": \"555-12345\"}},\n" +
            "{\"name\": \"Ann\",\"contact\": {\"mail\": \"Ann@convotis.com\", \"phone\": \"555-434343\"}}\n" +
            "]";

    public static final String ARRAY_JSON_WITH_MISSING_ELEMENTS = "[\n" +
            "{\"name\": \"Pat\",\"contact\": {\"town\": \"Bratislava\",\"mail\": \"John@convotis.com\",\"phone\": \"555-12345\"}},\n" +
            "{\"name\": \"Ann\",\"contact\": {\"mail\": \"Ann@convotis.com\",\"phone\": \"555-434343\"}},\n" +
            "{\"name\": \"Tom\",\"contact\": {\"town\": \"Plzeň\",\"mail\": \"Tom@convotis.com\"}}\n" +
            "]";

    public static final String PLAIN_JSON = "{\"menu1\":  \"value1\", \"menu2\": \"value2\"}";
    public static final String JSON_SINGLE_OBJECT = "{\"menu\": { \"id\": \"file\", \"age\": 5  }}";

    public static final String bodyDescription = "Document to convert \nexample json strings:\n " +
            "\n<b>Array: ------------------------------------------</b>\n\n" + ARRAY_JSON +
            "\n<b>Array with missing elements: ------------------------------------------</b>\n\n" + ARRAY_JSON_WITH_MISSING_ELEMENTS +
            "\n\n<b>Plain json -------------------------------------</b>\n\n" + PLAIN_JSON +
            "\n\n<b>Single object ---------------------------------</b>\n\n" + JSON_SINGLE_OBJECT + "\n";
}
