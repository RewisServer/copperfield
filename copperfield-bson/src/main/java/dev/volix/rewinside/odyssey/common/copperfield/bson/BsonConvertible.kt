package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.convertFrom
import dev.volix.rewinside.odyssey.common.copperfield.convertTo
import org.bson.Document

/**
 * @author Benedikt Wüller
 */
interface BsonConvertible {

    @JvmDefault
    fun toBsonDocument(registry: ConverterRegistry<Document>): Document {
        val document = Document()
        convertTo(this, document, registry, BsonFieldNameMapper, BsonFieldFilter)
        return document
    }

    @JvmDefault
    fun fromBsonDocument(source: Document, registry: ConverterRegistry<Document>) {
        convertFrom(this, source, registry, BsonFieldNameMapper, BsonFieldFilter)
    }

}
