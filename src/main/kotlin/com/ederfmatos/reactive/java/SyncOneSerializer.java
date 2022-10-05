package com.ederfmatos.reactive.java;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SyncOneSerializer extends StdSerializer<SyncOne<?>>{

    public SyncOneSerializer() {
        this(null);
    }

    protected SyncOneSerializer(Class<SyncOne<?>> t) {
        super(t);
    }

    @Override
    public void serialize(SyncOne<?> syncOne, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeObject(syncOne.value);
    }
}