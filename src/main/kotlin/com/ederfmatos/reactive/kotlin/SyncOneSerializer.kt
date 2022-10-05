package com.ederfmatos.reactive.kotlin

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class SyncOneSerializer : StdSerializer<SyncOne<*>>(SyncOne::class.java) {
    override fun serialize(syncOne: SyncOne<*>, jsonGenerator: JsonGenerator, provider: SerializerProvider?) {
        jsonGenerator.writeObject(syncOne.value)
    }
}