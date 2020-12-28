package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import org.bson.Document
import java.util.Date

/**
 * @author Benedikt Wüller
 */
class DateBsonConverter : BsonConverter<Date> {
    override fun convertFrom(name: String, source: Document): Date? = source.getDate(name)
}
