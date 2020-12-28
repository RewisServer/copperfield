package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class LongBsonConverter : BsonConverter<Long> {
    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): Long? = source.getLong(name)
}
