package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.BsonConvertableConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.ZonedDateTimeToStringConverter
import org.bson.Document
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Additional annotation: [CopperBsonField]. Use it in addition to [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField]
 * or as an alternative.
 *
 * Additional [dev.volix.rewinside.odyssey.common.copperfield.converter.Converter]s:
 *   - [BsonConvertable] using [BsonConvertableConverter]
 *   - [ByteArray] using [ByteArrayToBsonBinaryConverter]
 *   - [ZonedDateTime] using [ZonedDateTimeToStringConverter] and [DateTimeFormatter.ISO_OFFSET_DATE_TIME]
 *
 * @see Registry
 *
 * @author Benedikt Wüller
 */
open class BsonRegistry : Registry<BsonConvertable, Document>(BsonConvertable::class.java, Document::class.java) {

    init {
        // Register additional annotation.
        this.registerAnnotation(CopperBsonField::class.java)

        // Register additional/replacement converters.
        this.setDefaultConverter(BsonConvertable::class.java, BsonConvertableConverter::class.java)
        this.setDefaultConverter(ByteArray::class.java, ByteArrayToBsonBinaryConverter::class.java)
        this.setDefaultConverter(ZonedDateTime::class.java, ZonedDateTimeToStringConverter(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

}
