package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidBsonConverter : BsonConverter<UUID> {

    override fun convertTo(name: String, value: UUID?, target: Document, field: Field, registry: ConverterRegistry<Document>) {
        target[name] = value?.toString()
    }

    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): UUID? {
        val string = source.getString(name) ?: return null
        return UUID.fromString(string)
    }

}
