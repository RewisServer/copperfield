package dev.volix.rewinside.odyssey.common.copperfield.bson

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class StringBsonConverter : BsonConverter<String> {
    override fun convertFrom(name: String, source: Document): String? = source.getString(name)
}
