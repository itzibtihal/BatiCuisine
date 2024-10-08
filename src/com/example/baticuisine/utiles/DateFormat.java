package com.example.baticuisine.utiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, dateFormat);
    }
}