package dev.volix.rewinside.odyssey.common.copperfield.protobuf.typeconverter

import com.google.protobuf.Timestamp
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry
import java.lang.reflect.Field
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Benedikt Wüller
 */
class ZonedDateTimeProtoTypeConverter(private val timeZone: ZoneId)
    : ProtoTypeConverter<ZonedDateTime, Timestamp>(ZonedDateTime::class.java, Timestamp::class.java) {

    override fun convertOursToTheirs(value: ZonedDateTime?, field: Field, registry: ProtoRegistry): Timestamp? {
        if (value == null) return null
        val instant = value.toInstant()
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }

    override fun convertTheirsToOurs(value: Timestamp?, field: Field, registry: ProtoRegistry): ZonedDateTime? {
        if (value == null) return null
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.seconds, value.nanos.toLong()), this.timeZone)
    }

}
