package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.convertFrom
import dev.volix.rewinside.odyssey.common.copperfield.convertTo
import org.bson.Document

/**
 * @author Benedikt Wüller
 */
interface BsonConvertible {

    @JvmDefault
    fun toBsonDocument(registry: BsonRegistry): Document {
        val document = Document()
        convertTo(this, document, registry)
        return document
    }

    @JvmDefault
    fun fromBsonDocument(source: Document, registry: BsonRegistry) {
        convertFrom(this, source, registry)
    }

}
