package kc.web.converter;

import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ApplicationConstant;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

public class StringToUUIDConverter implements Converter<String, UUID> {
    public UUID convert(String source) {
        if(StringExtensions.isNullOrEmpty(source))
            return ApplicationConstant.UUID_NIL;
        return UUID.fromString(source);
    }
}
