package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ZonedDateTimeToProtoTimestampConverter
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : Registry<ProtoConvertable<*>, MessageOrBuilder>(ProtoConvertable::class.java, MessageOrBuilder::class.java) {

    init {
        this.registerAnnotation(CopperProtoField::class.java)
        this.setConverter(ZonedDateTime::class.java, ZonedDateTimeToProtoTimestampConverter(ZoneId.of("Europe/Berlin")))
    }

    override fun createTheirs(convertible: ProtoConvertable<*>): MessageOrBuilder {
        val annotation = convertible.javaClass.getDeclaredAnnotation(CopperProtoType::class.java)
            ?: throw IllegalStateException("The annotation @CopperProtoType is required for ProtoConvertable: ${convertible.javaClass}")

        val type = annotation.type.java
        val newBuilderMethod = type.getDeclaredMethod("newBuilder")
        return newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>
    }

    override fun <T : MessageOrBuilder> finalizeTheirs(instance: T): MessageOrBuilder {
        return instance.javaClass.getDeclaredMethod("build").invoke(instance) as MessageOrBuilder
    }

    override fun readValue(name: String, entity: MessageOrBuilder, type: Class<out Any>): Any? {
        val methodName = when {
            Iterable::class.java.isAssignableFrom(type) -> "get${name.snakeToPascalCase()}List"
            else -> "get${name.snakeToPascalCase()}"
        }

        val method = entity.javaClass.getDeclaredMethod(methodName)
            ?: throw RuntimeException("Unable to find getter method for $type.")

        return method.invoke(entity)
    }

    override fun writeValue(name: String, value: Any?, entity: MessageOrBuilder, type: Class<out Any>) {
        val methodName = when {
            Iterable::class.java.isAssignableFrom(type) -> "addAll${name.snakeToPascalCase()}"
            else -> "set${name.snakeToPascalCase()}"
        }

        val method = entity.javaClass.declaredMethods.firstOrNull { method ->
            if (method.name != methodName) return@firstOrNull false
            if (method.parameterCount != 1) return@firstOrNull false
            method.parameterTypes.first().isAssignableFrom(type)
        } ?: throw RuntimeException("Unable to find setter method for $type.")

        method.invoke(entity, value)
    }

}
