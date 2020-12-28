package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import org.bson.Document
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class UuidBsonConverter : BsonConverter<UUID> {

    override fun convertTo(name: String, value: UUID?, target: Document) {
        target[name] = value?.toString()
    }

    override fun convertFrom(name: String, source: Document): UUID? {
        val string = source.getString(name) ?: return null
        return UUID.fromString(string)
    }

}
