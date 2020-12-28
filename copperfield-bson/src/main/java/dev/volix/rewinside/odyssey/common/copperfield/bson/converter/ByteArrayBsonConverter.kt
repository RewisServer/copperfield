package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import org.bson.types.Binary
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ByteArrayBsonConverter : BsonConverter<ByteArray> {

    override fun convertTo(name: String, value: ByteArray?, target: Document, field: Field, registry: ConverterRegistry<Document>) {
        target[name] = if (value == null) null else Binary(value)
    }

    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): ByteArray? {
        val value = source.get(name, Binary::class.java) ?: return null
        return value.data
    }

}
