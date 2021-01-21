package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import java.lang.reflect.Field
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Benedikt Wüller
 */
class OffsetDateTimeToStringConverter(private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    : Converter<OffsetDateTime, String>(OffsetDateTime::class.java, String::class.java) {

    override fun toTheirs(
        value: OffsetDateTime?, agent: CopperfieldAgent, ourType: Class<out OffsetDateTime>, targetFormat: Class<Any>,
        field: Field?): String? {
        return value?.format(this.formatter)
    }

    override fun toOurs(
        value: String?, agent: CopperfieldAgent, ourType: Class<out OffsetDateTime>, targetFormat: Class<Any>,
        field: Field?): OffsetDateTime? {
        return if (value == null) null else OffsetDateTime.parse(value, this.formatter)
    }

}
