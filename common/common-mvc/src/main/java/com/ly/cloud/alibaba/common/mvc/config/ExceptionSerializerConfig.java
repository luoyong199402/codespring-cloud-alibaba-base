package com.ly.cloud.alibaba.common.mvc.config;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.*;

@JsonComponent
public class ExceptionSerializerConfig {
    public static class Serializer extends JsonSerializer<Exception> {
        @Override
        public void serialize(Exception exception, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(byteArrayOutputStream);
            oo.writeObject(exception);
            oo.flush();
            oo.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            gen.writeRawValue("\"" + ByteArrayUtil.toHexString(bytes) + "\"");
        }
    }

    public static class Deserializer extends JsonDeserializer<Exception> {
        @Override
        public Exception deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String valueAsString = jsonParser.getValueAsString();
            byte[] bytes = ByteArrayUtil.hexStringToByteArray(valueAsString);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
            try {
                Exception ex = (Exception) ois.readObject();
                return ex;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                ois.close();
            }
        }
    }
}
