package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Benedikt Wüller
 */
class ZonedDateTimeToStringConverter : Converter<ZonedDateTime, String>(ZonedDateTime::class.java, String::class.java) {

    override fun toTheirs(value: ZonedDateTime?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): String? {
        return value?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    override fun toOurs(value: String?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): ZonedDateTime? {
        return if (value == null) null else ZonedDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

}
