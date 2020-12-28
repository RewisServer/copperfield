package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class BooleanBsonConverter : BsonConverter<Boolean> {
    override fun convertFrom(name: String, source: Document): Boolean? = source.getBoolean(name)
}
