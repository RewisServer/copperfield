package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.MessageLiteOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ByteArrayToByteStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.MapToProtoStructConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ProtoConvertableConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ZonedDateTimeToProtoTimestampConverter
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Additional annotation: [CopperProtoField]. Use it in addition to [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * or as an alternative.
 *
 * Additional/replacement [dev.volix.rewinside.odyssey.common.copperfield.converter.Converter]s:
 *   - [ProtoConvertable] using [ProtoConvertableConverter]
 *   - [Map] using [MapToProtoStructConverter]
 *   - [ByteArray] using [ByteArrayToByteStringConverter]
 *   - [ZonedDateTime] using [ZonedDateTimeToProtoTimestampConverter] with timezone `Europe/Berlin`.
 *
 * @see Registry
 *
 * @author Benedikt Wüller
 */
open class ProtoRegistry : Registry<ProtoConvertable<*>, MessageLiteOrBuilder>(ProtoConvertable::class.java, MessageLiteOrBuilder::class.java) {

    init {
        // Register additional annotation.
        this.registerAnnotation(CopperProtoField::class.java)

        // Register additional/replacement converters.
        this.setDefaultConverter(ProtoConvertable::class.java, ProtoConvertableConverter::class.java)
        this.setDefaultConverter(Map::class.java, MapToProtoStructConverter::class.java)
        this.setDefaultConverter(ByteArray::class.java, ByteArrayToByteStringConverter::class.java)
        this.setDefaultConverter(ZonedDateTime::class.java, ZonedDateTimeToProtoTimestampConverter(ZoneId.of("Europe/Berlin")))
    }

}
