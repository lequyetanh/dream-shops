package com.dailycodework.dreamshops.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// common/util/TupleMapper.java
@Slf4j
@UtilityClass
public class TupleMapper {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Bỏ qua field trong JSON không có trong DTO
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Map NativeQuery result → DTO.
     * Tự xử lý mọi type conversion qua Jackson.
     * Alias trong SQL phải khớp camelCase với field name DTO.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> mapList(Query query, Class<T> dtoClass) {
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> rows = query.getResultList();

        return rows.stream()
                .map(row -> convertRow(row, dtoClass))
                .collect(Collectors.toList());
    }

    private static <T> T convertRow(Map<String, Object> row, Class<T> dtoClass) {
        // Normalize value trước khi convert (xử lý các type DB-specific)
        Map<String, Object> normalized = normalizeDatabaseTypes(row);
        return OBJECT_MAPPER.convertValue(normalized, dtoClass);
    }

    /**
     * Normalize các type DB-specific → Java standard type.
     * Thêm case mới ở đây khi gặp type mới — không cần sửa chỗ khác.
     */
    private static Map<String, Object> normalizeDatabaseTypes(Map<String, Object> row) {
        Map<String, Object> result = new LinkedHashMap<>();
        row.forEach((key, value) -> result.put(key, normalizeValue(value)));
        return result;
    }

    private static Object normalizeValue(Object value) {
        if (value == null) return null;

        // MSSQL DateTimeOffset
        if (value instanceof microsoft.sql.DateTimeOffset dto) {
            return dto.getOffsetDateTime().toString(); // Jackson parse được ISO string
        }
        // OffsetDateTime → ISO string
        if (value instanceof OffsetDateTime odt) {
            return odt.toString();
        }
        // LocalDateTime → ISO string
        if (value instanceof LocalDateTime ldt) {
            return ldt.atZone(ZoneId.systemDefault()).toString();
        }
        // Các type khác Jackson tự xử lý
        return value;
    }
}