package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.lang.reflect.Field
import java.util.Date

/**
 * @author Benedikt Wüller
 */
class DateBsonConverter : SimpleBsonConverter<Date>() {
    override fun convertFrom(name: String, source: Document, field: Field, registry: ConverterRegistry<Document>): Date? = source.getDate(name)
}
