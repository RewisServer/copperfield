package dev.volix.rewinside.odyssey.common.copperfield.bson

import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class DoubleBsonConverter : BsonConverter<Double> {
    override fun convertFrom(name: String, source: Document): Double? = source.getDouble(name)
}
