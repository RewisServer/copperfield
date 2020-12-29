package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.Timestamp
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Benedikt Wüller
 */
class ZonedDateTimeToProtoTimestampConverter(private val timeZone: ZoneId) : Converter<ZonedDateTime, Timestamp>(ZonedDateTime::class.java, Timestamp::class.java) {

    override fun toTheirs(value: ZonedDateTime?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): Timestamp? {
        val instant = value?.toInstant() ?: return null
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }

    override fun toOurs(value: Timestamp?, field: Field?, registry: Registry<*, *>, type: Class<out ZonedDateTime>): ZonedDateTime? {
        if (value == null) return null
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.seconds, value.nanos.toLong()), this.timeZone)
    }

}
