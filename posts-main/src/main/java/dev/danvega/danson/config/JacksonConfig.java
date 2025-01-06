package dev.danvega.danson.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Support for Java 8 time API (e.g., LocalDateTime)
        mapper.registerModule(new JavaTimeModule());

        // Disable timestamp arrays, enforce ISO-8601 format
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Ignore unknown JSON fields during deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Include non-null fields only in serialization
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Format output JSON for readability (optional)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
