package com.suragreat.em.biz.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.suragreat.em.biz.address.model.Area;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AreaReader {
    CsvSchema schema = CsvSchema.emptySchema().withHeader();

    public AreaReader() {
    }

    public MappingIterator<Area> read() throws IOException {
        InputStream inputStream = AreaReader.class.getClassLoader().getResourceAsStream("china.csv");
        CsvMapper csvMapper = new CsvMapper();
        return csvMapper.readerFor(Area.class).with(schema).readValues(inputStream);
    }

}
