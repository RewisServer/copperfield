package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class FloatBsonConverter : BsonConverter<Float> {
    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): Float? = source.getDouble(name)?.toFloat()
}
