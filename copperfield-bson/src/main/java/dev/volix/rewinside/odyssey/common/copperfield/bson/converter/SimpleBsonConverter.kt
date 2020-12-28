package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class SimpleBsonConverter<T : Any> : BsonConverter<T> {

    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): T? {
        return source.get(name, field.type) as T?
    }

    override fun convertTo(name: String, value: T?, target: Document, field: Field, registry: ConverterRegistry<Document>) {
        target[name] = value
    }

}
