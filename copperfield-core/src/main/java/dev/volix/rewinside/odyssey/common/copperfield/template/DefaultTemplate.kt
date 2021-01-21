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
open class DefaultTemplate : Template() {

    init {
        this.with(Iterable::class.java, IterableConverter())
        this.with(Map::class.java, MapConverter())
        this.with(Number::class.java, NumberConverter())
        this.with(UUID::class.java, UuidToStringConverter())
        this.with(Enum::class.java, EnumToStringConverter())
        this.with(OffsetDateTime::class.java, OffsetDateTimeToStringConverter())
    }

}
