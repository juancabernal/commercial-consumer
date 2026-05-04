package com.eatup.commercial.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MapperJsonObjetoJackson implements MapperJsonObjeto {

    private final ObjectMapper objectMapper;

    public MapperJsonObjetoJackson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> Optional<T> ejecutar(String json, Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
