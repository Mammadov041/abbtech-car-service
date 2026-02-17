package com.abbtech.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.List;
import java.util.stream.Collectors;

public class LogSanitizer {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(
                    com.fasterxml.jackson.databind.SerializationConfig config,
                    com.fasterxml.jackson.databind.BeanDescription beanDesc,
                    List<BeanPropertyWriter> beanProperties) {

                return beanProperties.stream()
                        .filter(writer -> writer.getMember()
                                .getAnnotation(LogIgnore.class) == null)
                        .collect(Collectors.toList());
            }
        });

        mapper.registerModule(module);
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "Could not serialize";
        }
    }
}
