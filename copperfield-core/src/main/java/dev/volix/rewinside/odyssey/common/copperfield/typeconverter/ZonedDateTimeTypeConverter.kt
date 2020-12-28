package dev.volix.rewinside.odyssey.common.copperfield.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Benedikt Wüller
 */
class ZonedDateTimeTypeConverter<R : Registry<*, *, *>>
    : TypeConverter<R, ZonedDateTime, String>(ZonedDateTime::class.java, String::class.java) {

    override fun convertOursToTheirs(value: ZonedDateTime, field: Field, registry: R): String = value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    override fun convertTheirsToOurs(value: String, field: Field, registry: R): ZonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

}
