package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertible
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ConvertibleBsonConverter : BsonConverter<BsonConvertible> {

    override fun convertTo(name: String, value: BsonConvertible?, target: Document, field: Field, registry: ConverterRegistry<Document>) {
        target[name] = value?.toBsonDocument(registry)
    }

    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): BsonConvertible? {
        val document = source.get(name, Document::class.java) ?: return null
        val type = field.type
        val instance = type.newInstance() as BsonConvertible
        instance.fromBsonDocument(document, registry)
        return instance
    }

}
