package com.wallhack.weathermap.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MillisOrLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

    public MillisOrLocalDateTimeDeserializer() {
        super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        long timestampInSeconds = parser.getValueAsLong();

        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestampInSeconds),
                ZoneId.systemDefault());
    }
}
