package com.svlugovoy.weathertelegrambot;

import lombok.Data;

@Data
public class ResultDto {
    private String name;
    private String sysCountry;
    private String weatherDescription;
    private String mainTemp;
    private String minTemp;
    private String maxTemp;
    private String windSpeed;
}
