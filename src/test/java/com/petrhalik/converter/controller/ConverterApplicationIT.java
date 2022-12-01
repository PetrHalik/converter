package com.petrhalik.converter.controller;

import com.petrhalik.converter.ConverterApplication;
import com.petrhalik.converter.testutils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ConverterApplication.class)
public class ConverterApplicationIT {

    public static final String CSV_CONTENT_TYPE = "text/csv;charset=UTF-8";
    public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testConverterJsonArray_shouldReturnCorrectResult() throws Exception {
        String expectedDocument = "name,contact.mail,contact.phone\n" +
                "Pat,John@convotis.com,555-12345\n" +
                "Ann,Ann@convotis.com,555-434343\n";

        String body = mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "csv")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ConverterController.ARRAY_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(CSV_CONTENT_TYPE))
                .andExpect(header().exists(CONTENT_DISPOSITION_HEADER))
                .andReturn().getResponse().getContentAsString();

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(body));
    }


    @Test
    public void testConverterJsonArrayWithMissingElements_shouldReturnCorrectResult() throws Exception {
        String expectedDocument = "name,contact.town,contact.mail,contact.phone\n" +
                "Pat,Bratislava,John@convotis.com,555-12345\n" +
                "Ann,,Ann@convotis.com,555-434343\n" +
                "Tom,Plzeň,Tom@convotis.com,\n";

        String body = mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "csv")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ConverterController.ARRAY_JSON_WITH_MISSING_ELEMENTS)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(CSV_CONTENT_TYPE))
                .andExpect(header().exists(CONTENT_DISPOSITION_HEADER))
                .andReturn().getResponse().getContentAsString();

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(body));
    }

    @Test
    public void testConverterJsonSingleObject_shouldReturnCorrectResult() throws Exception {
        String expectedDocument = "menu.id,menu.age\n" +
                "file,5\n";

        String body = mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "csv")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ConverterController.JSON_SINGLE_OBJECT)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(CSV_CONTENT_TYPE))
                .andExpect(header().exists(CONTENT_DISPOSITION_HEADER))
                .andReturn().getResponse().getContentAsString();

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(body));
    }

    @Test
    public void testConverterPlainJson_shouldReturnCorrectResult() throws Exception {
        String expectedDocument = "menu1,menu2\n" +
                "value1,\n" +
                ",value2\n";

        String body = mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "csv")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ConverterController.PLAIN_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(CSV_CONTENT_TYPE))
                .andExpect(header().exists(CONTENT_DISPOSITION_HEADER))
                .andReturn().getResponse().getContentAsString();

        assertEquals(TestUtils.normalizeEOL(expectedDocument), TestUtils.normalizeEOL(body));
    }

    @Test
    public void testConverterNotJsonDocument_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "csv")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content("aaaaa")
                ).andExpect(status().isBadRequest());
    }

    @Test
    public void testConverterWrongOutputFormat_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                        post("/converter/json")
                                .param("outputFormat", "xml")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ConverterController.PLAIN_JSON)
                ).andExpect(status().isNotAcceptable());
    }

    @Test
    public void testConverterMalformedJson_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                post("/converter/json")
                        .param("outputFormat", "csv")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{ \"name\": 1 ")
        ).andExpect(status().isBadRequest());
    }
}
