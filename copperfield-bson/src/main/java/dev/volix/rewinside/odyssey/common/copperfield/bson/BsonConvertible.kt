package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Convertible
import org.bson.Document

/**
 * @author Benedikt Wüller
 */
interface BsonConvertible : Convertible<Document> {

    @JvmDefault
    fun toBsonDocument(registry: BsonRegistry): Document {
        val document = Document()
        val fields = this.findFields(registry)
        fields.forEach {
            val converter = it.converter
            val name = it.name
            val value = it.field.get(this)
            converter.convertTo(name, value, document)
        }
        return document
    }

    @JvmDefault
    fun fromBsonDocument(source: Document, registry: BsonRegistry) {
        val fields = this.findFields(registry)
        fields.forEach {
            val converter = it.converter
            val name = it.name
            val value = converter.convertFrom(name, source)
            it.field.set(this, value)
        }
    }

}
