package kc.framework.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import kc.framework.extension.StringExtensions;

@lombok.extern.slf4j.Slf4j
public class SerializeHelper {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        //允许出现特殊字符和转义符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        //允许出现单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //大小写脱敏 默认为false
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 在序列化时忽略值为 null 的属性
        mapper.setSerializationInclusion(Include.NON_NULL);
        // 忽略值为默认值的属性
        mapper.setDefaultPropertyInclusion(Include.NON_DEFAULT);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static <T> String ToJson(T t) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T FromJson(String strJson, Class<T> clazz) {
        if (!StringExtensions.isNullOrEmpty(strJson)) {
            try {
                return mapper.readValue(strJson, clazz);
            } catch (JsonParseException e) {
                log.error(e.getMessage(), e);
            } catch (JsonMappingException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static <T> T FromJson(String strJson, TypeReference<T> type) {
        if (!StringExtensions.isNullOrEmpty(strJson)) {
            try {
                return mapper.readValue(strJson, type);
            } catch (JsonParseException e) {
                log.error(e.getMessage(), e);
            } catch (JsonMappingException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static Map getMapByJson(String json) {
        if (StringExtensions.isNullOrEmpty(json)) {
            return null;
        }

        try {
            return mapper.readValue(json, Map.class);
        } catch (Exception var2) {
            if (log.isErrorEnabled()) {
                log.error(var2.getMessage(), var2);
                log.error("getMapByJson error: " + json);
            }

            return null;
        }
    }

    public static String getMapValueByKey(String json, String key) {
        if (StringExtensions.isNullOrEmpty(json)) {
            return null;
        }

        try {
            JsonNode mapResult = mapper.readTree(json);
            return read(mapResult, key);
        } catch (Exception var2) {
            if (log.isErrorEnabled()) {
                log.error(var2.getMessage(), var2);
                log.error("getMapByJson error: " + json);
            }

            return null;
        }
    }

    private static String read(JsonNode node, String key) {
        if (null == node || StringExtensions.isNullOrEmpty(key))
            return null;

        if (null != node.get(key)) {
            return node.get(key).toString();
        }

        String result = null;
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                result = read(entry.getValue(), key);
                if (null != result)
                    return result;
            }
        }

        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                result = read(jsonNode, key);
                if (null != result)
                    return result;
            }
        }

        return result;
    }

    public static  <T> T  getMapObjectByKey(String json, String key, Class<T> clazz) {
        if (StringExtensions.isNullOrEmpty(json)) {
            return null;
        }

        try {
            JsonNode mapResult = mapper.readTree(json);
            return readObject(mapResult, key, clazz);
        } catch (Exception var2) {
            if (log.isErrorEnabled()) {
                log.error(var2.getMessage(), var2);
                log.error("getMapByJson error: " + json);
            }

            return null;
        }
    }

    private static <T> T readObject(JsonNode node, String key, Class<T> clazz) {
        if (null == node || StringExtensions.isNullOrEmpty(key))
            return null;

        if (null != node.get(key)) {
            try {
                return mapper.treeToValue(node.get(key), clazz);
            } catch (JsonParseException e) {
                log.error(e.getMessage(), e);
            } catch (JsonMappingException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        T result = null;
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                result = readObject(entry.getValue(), key, clazz);
                if (null != result)
                    return result;
            }
        }

        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                result = readObject(jsonNode, key, clazz);
                if (null != result)
                    return result;
            }
        }

        return result;
    }
}
