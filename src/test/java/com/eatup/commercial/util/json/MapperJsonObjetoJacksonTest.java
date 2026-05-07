package com.eatup.commercial.util.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class MapperJsonObjetoJacksonTest {

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    private final MapperJsonObjeto mapperJsonObjeto = new MapperJsonObjetoJackson(objectMapper);

    @Test
    void ejecutarConJsonValidoDebeRetornarOptionalPresente() {
        String saleId = UUID.randomUUID().toString();
        String json = "{\"saleId\":\"" + saleId + "\",\"request\":{\"status\":\"COMPLETED\"}}";

        Optional<SalePatchRequestedMessage> result = mapperJsonObjeto.ejecutar(json, SalePatchRequestedMessage.class);

        assertTrue(result.isPresent());
        assertEquals(saleId, result.get().getSaleId().toString());
        assertEquals("COMPLETED", result.get().getRequest().getStatus());
    }

    @Test
    void ejecutarConJsonInvalidoDebeRetornarOptionalVacio() {
        Optional<SalePatchRequestedMessage> result =
                mapperJsonObjeto.ejecutar("{\"saleId\":", SalePatchRequestedMessage.class);

        assertTrue(result.isEmpty());
    }
}
