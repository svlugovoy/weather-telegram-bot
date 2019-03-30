package com.svlugovoy.weathertelegrambot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParserService {

    @Autowired
    private ObjectMapper mapper;

    public ResultDto parse(String str) throws IOException {

        JsonNode root = mapper.readTree(str);

        JsonNode name = root.path("name");

        JsonNode sys = root.path("sys");
        JsonNode country = sys.path("country");

        JsonNode weather = root.path("weather").get(0);
        JsonNode description = weather.path("description");

        JsonNode main = root.path("main");
        JsonNode temp = main.path("temp");
        JsonNode min = main.path("temp_min");
        JsonNode max = main.path("temp_max");

        JsonNode wind = root.path("wind");
        JsonNode speed = wind.path("speed");

        ResultDto resultDto = new ResultDto();
        resultDto.setName(name.asText());
        resultDto.setSysCountry(country.asText());
        resultDto.setWeatherDescription(description.asText());
        resultDto.setMainTemp(temp.asText());
        resultDto.setMinTemp(min.asText());
        resultDto.setMaxTemp(max.asText());
        resultDto.setWindSpeed(speed.asText());

        return resultDto;
    }

}
