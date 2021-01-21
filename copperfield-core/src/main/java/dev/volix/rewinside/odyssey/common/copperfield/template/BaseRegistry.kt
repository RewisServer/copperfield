package dev.volix.rewinside.odyssey.common.copperfield.template

import dev.volix.rewinside.odyssey.common.copperfield.converter.EnumToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.IterableConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.MapConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.NumberConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.OffsetDateTimeToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.UuidToStringConverter
import java.time.OffsetDateTime
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
open class BaseRegistry : Registry() {

    init {
        this.with(Iterable::class.java, IterableConverter::class.java)
        this.with(Map::class.java, MapConverter::class.java)
        this.with(Number::class.java, NumberConverter::class.java)
        this.with(UUID::class.java, UuidToStringConverter::class.java)
        this.with(Enum::class.java, EnumToStringConverter::class.java)
        this.with(OffsetDateTime::class.java, OffsetDateTimeToStringConverter::class.java)
    }

}
