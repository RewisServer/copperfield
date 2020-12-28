package dev.volix.rewinside.odyssey.common.copperfield.bson

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class FloatBsonConverter : BsonConverter<Float> {
    override fun convertFrom(name: String, source: Document): Float? = source.getDouble(name)?.toFloat()
}
