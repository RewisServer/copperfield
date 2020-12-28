package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConversionDirection
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoIgnore
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : Registry<MessageOrBuilder, ProtoConvertible, ProtoRegistry>(MessageOrBuilder::class.java, ProtoConvertible::class.java) {

    // TODO
    //  - list entry conversion

    init {
//        this.setTypeConverter(ZonedDateTime::class.java, ZonedDateTimeProtoTypeConverter(ZoneId.of("Europe/Berlin")))
    }

    override fun <A : MessageOrBuilder> write(entity: ProtoConvertible?, type: Class<A>): A? {
        if (entity == null) return null

        // TODO: cache this stuff

        val newBuilderMethod = type.getDeclaredMethod("newBuilder")
        val builder = newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>

        this.getFields(entity.javaClass, ConversionDirection.SERIALIZE).forEach { (field, name) ->
            // This is fucking ugly but required for casting purposes.
            val methodName = if (List::class.java.isAssignableFrom(field.type)) {
                "addAll${name.snakeToPascalCase()}"
            } else {
                "set${name.snakeToPascalCase()}"
            }

            // Find the method.
            val method = builder.javaClass.declaredMethods.firstOrNull { it.name == methodName }
            if (method == null || method.parameterTypes.isEmpty()) {
                TODO("throw exception")
            }

            val methodType = method.parameterTypes[0]
            val converter = this.getConverter(field.type)
            val value = methodType.cast(converter.convertOursToTheirs(field.get(entity), field, this))
            method.invoke(builder, value)
        }

        val buildMethod = builder.javaClass.getDeclaredMethod("build")
        return buildMethod.invoke(builder) as A?
    }

    override fun <A : ProtoConvertible> read(entity: MessageOrBuilder?, type: Class<A>): A? {
        if (entity == null) return null
        val instance = type.newInstance()
        this.getFields(type, ConversionDirection.DESERIALIZE).forEach { (field, name) ->
            val methodName = if (List::class.java.isAssignableFrom(field.type)) {
                "get${name.snakeToPascalCase()}List"
            } else {
                "get${name.snakeToPascalCase()}"
            }

            val method = entity.javaClass.getDeclaredMethod(methodName)
            val value = method.invoke(entity)

            val converter = this.getConverter(field.type)
            val convertedValue = converter.convertTheirsToOurs(value, field, this)

            field.set(instance, convertedValue)
        }
        return instance
    }

    override fun isIgnored(field: Field, direction: ConversionDirection): Boolean {
        val annotation = field.getDeclaredAnnotation(CopperProtoIgnore::class.java) ?: return false
        return when (direction) {
            ConversionDirection.SERIALIZE -> annotation.ignoreSerialize
            ConversionDirection.DESERIALIZE -> annotation.ignoreDeserialize
            else -> false
        }
    }

    override fun isAnnotated(field: Field) = super.isAnnotated(field) || field.isAnnotationPresent(CopperProtoField::class.java)

    override fun getName(field: Field): String? {
        var name = field.getDeclaredAnnotation(CopperProtoField::class.java)?.name
        if (name.isNullOrEmpty()) {
            name = super.getName(field)
        }
        return name
    }

}
