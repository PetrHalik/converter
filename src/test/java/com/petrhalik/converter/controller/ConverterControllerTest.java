package com.petrhalik.converter.controller;

import com.petrhalik.converter.domain.converter.model.ConversionResult;
import com.petrhalik.converter.domain.converter.model.InputType;
import com.petrhalik.converter.domain.converter.model.OutputType;
import com.petrhalik.converter.service.IConverterService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class ConverterControllerTest {

    private MockMvc mockMvc;

    IConverterService converterService;

    @Before
    public void setup() {
        converterService = mock(IConverterService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ConverterController(converterService)).build();
    }

    @Test
    public void testConverter() throws Exception {
        ConversionResult responseMock = new ConversionResult("aaa", "filename", "text/csv;charset=UTF-8");

        when(converterService.convertDocument(InputType.json, OutputType.csv,"aaaaa")).thenReturn(responseMock);

        mockMvc.perform(
                post("/converter/json")
                        .param("outputFormat", "csv")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("aaaaa")
                ).andExpect(status().isOk())
                .andExpect(content().contentType("text/csv;charset=UTF-8"))
                .andExpect(header().exists("Content-Disposition"));

    }
}