package com.dailycodework.dreamshops.util;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Common {
    private static final String ENTITY_NAME = "dream.shops.common";
    private static final Logger log = LoggerFactory.getLogger(ENTITY_NAME);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setParamsWithPageable(
            @NotNull Query query,
            Map<String, Object> params,
            @NotNull Pageable pageable,
            @NotNull Number total
    ) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static String normalizedTime(ZonedDateTime datetime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(BaseConstant.NORMALIZED_TIME_FORMAT);
        return datetime.format(outputFormatter);
    }

    public static String normalizedTime(String datetime, String pattern) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(pattern);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(BaseConstant.NORMALIZED_TIME_FORMAT);

            LocalDateTime parsedDateTime = LocalDateTime.parse(datetime, inputFormatter);
            return parsedDateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime or pattern: " + e.getMessage());
        }
    }

    public static Integer normalizedTime(ZonedDateTime datetime, String pattern) {
        try {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(pattern);
            return Integer.parseInt(datetime.format(outputFormatter));
        } catch (Exception ex) {
            log.error("Error get norm_date");
        }
        return null;
    }

    public static ZonedDateTime convertStringToZoneDateTime(String dateTime, String pattern) {
        if (!Strings.isNullOrEmpty(dateTime)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
                return ZonedDateTime.parse(dateTime.replace("/", "-"), formatter);
            } catch (Exception ex) {
//                throw new BaseBadRequestException(
//                        ENTITY_NAME,
//                        BaseConstant.DATE_TIME_INVALID
//                );
            }
        }
        return null;
    }

    public static String toJsonString(Object object) {
        if (Objects.nonNull(object)) {
            try {
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("[ERROR_CONVERT_OBJECT], exception message: {}", e.getMessage());
                return "{}";
            }
        }
        return "{}";
    }

    public static <T> T fromJsonString(String json, Class<T> clazz) {
        if (Objects.nonNull(json) && !json.isBlank()) {
            try {
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                log.error("[ERROR_PARSE_JSON], exception message: {}", e.getMessage());
            }
        }
        return null;
    }


}
