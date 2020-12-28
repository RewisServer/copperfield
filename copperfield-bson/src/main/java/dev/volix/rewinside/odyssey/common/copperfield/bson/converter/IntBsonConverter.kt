package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class IntBsonConverter : BsonConverter<Int> {
    override fun convertFrom(name: String, source: Document): Int? = source.getLong(name)?.toInt()
}
