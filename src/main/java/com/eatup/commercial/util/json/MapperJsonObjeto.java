package com.eatup.commercial.util.json;

import java.util.Optional;

public interface MapperJsonObjeto {
    <T> Optional<T> ejecutar(String json, Class<T> clazz);
}
