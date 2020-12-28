package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.Timestamp
import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.time.Instant
import java.util.Date

/**
 * @author Benedikt Wüller
 */
class DateProtoConverter : ProtoReflectionConverter<Date>() {

    override fun convertTo(name: String, value: Date?, target: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>) {
        val timestamp = if (value == null) null else Timestamp.newBuilder().setSeconds(value.toInstant().epochSecond).build()
        this.getSetterMethod(name, target.javaClass, field).invoke(target, timestamp)
    }

    override fun convertFrom(name: String, source: MessageOrBuilder, field: Field, registry: ConverterRegistry<MessageOrBuilder>): Date? {
        val value = this.getGetterMethod(name, source.javaClass).invoke(source) as Timestamp? ?: return null
        return Date.from(Instant.ofEpochSecond(value.seconds))
    }

    override fun getSetterMethod(name: String, type: Class<MessageOrBuilder>, field: Field): Method {
        return type.getDeclaredMethod(this.getSetterMethodName(name), Timestamp::class.java)
    }

}
