package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Converts [ZonedDateTime]s to strings using the given [formatter] which defaults to [DateTimeFormatter.ISO_OFFSET_DATE_TIME].
 *
 * @author Benedikt Wüller
 */
open class ZonedDateTimeToStringConverter(private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME) : Converter<ZonedDateTime, String>(ZonedDateTime::class.java, String::class.java) {

    override fun toTheirs(value: ZonedDateTime?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): String? {
        return value?.format(this.formatter)
    }

    override fun toOurs(value: String?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): ZonedDateTime? {
        return if (value == null) null else ZonedDateTime.parse(value, this.formatter)
    }

}
