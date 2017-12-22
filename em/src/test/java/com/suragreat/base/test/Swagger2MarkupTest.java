package com.suragreat.base.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.asciidoctor.AsciiDocDirectoryWalker;
import org.asciidoctor.Asciidoctor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class Swagger2MarkupTest {
    private static final String SRC_DOCS_ASCIIDOC = "src/docs/asciidoc";
    @Autowired
    private MockMvc mockMvc;
    private Asciidoctor asciidoctor;

    @Before
    public void initAsciidoctor() throws IOException {
        this.asciidoctor = Asciidoctor.Factory.create();
        ClassLoader classLoader = asciidoctor.getClass().getClassLoader();
        FileUtils.forceMkdir(new File(SRC_DOCS_ASCIIDOC));
        InputStream is = classLoader
                .getResourceAsStream("gems/asciidoctor-1.5.5/data/stylesheets/asciidoctor-default.css");
        FileWriter fw = new FileWriter(SRC_DOCS_ASCIIDOC + "/asciidoctor.css");
        IOUtils.copy(is, fw);
        IOUtils.closeQuietly(is, fw);
    }

    @Test
    public void convertToAsciiDoc() throws Exception {
        mockMvc.perform(get("/api-docs").accept(MediaType.APPLICATION_JSON)).andDo(Swagger2MarkupResultHandler
                .outputDirectory(SRC_DOCS_ASCIIDOC).withExamples(SRC_DOCS_ASCIIDOC + "/generated").build())
                .andExpect(status().isOk());
    }

    @After
    public void toHtml() {
        HashMap<String, Object> options = new HashMap<String, Object>();
        asciidoctor.convertDirectory(new AsciiDocDirectoryWalker(SRC_DOCS_ASCIIDOC), options);
    }
}