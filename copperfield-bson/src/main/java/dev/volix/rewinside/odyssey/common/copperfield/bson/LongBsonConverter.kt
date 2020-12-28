package dev.volix.rewinside.odyssey.common.copperfield.bson

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class LongBsonConverter : BsonConverter<Long> {
    override fun convertFrom(name: String, source: Document): Long? = source.getLong(name)
}
