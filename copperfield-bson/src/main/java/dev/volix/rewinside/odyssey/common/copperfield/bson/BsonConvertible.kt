package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Convertible
import org.bson.Document

/**
 * @author Benedikt Wüller
 */
interface BsonConvertible : Convertible<Document> {

    fun toBsonDocument(): Document {
        val document = Document()
        // TODO
        return document
    }

    fun fromBsonDocument(source: Document) {
        // TODO
    }

}
