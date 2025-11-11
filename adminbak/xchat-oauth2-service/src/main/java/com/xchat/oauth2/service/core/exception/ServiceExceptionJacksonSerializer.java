package com.xchat.oauth2.service.core.exception;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * @author liuguofu
 *         on 3/19/15.
 */
public class ServiceExceptionJacksonSerializer extends StdSerializer<ServiceException> {

    public ServiceExceptionJacksonSerializer() {
        super(ServiceException.class);
    }

    @Override
    public void serialize(ServiceException value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();

        StatusCode statusCode = value.getStatusCode();

        jgen.writeNumberField("code", statusCode.value());
        jgen.writeStringField("message", value.getMessage());
        if (value.getAdditionalInformation()!=null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        jgen.writeEndObject();
    }
}
