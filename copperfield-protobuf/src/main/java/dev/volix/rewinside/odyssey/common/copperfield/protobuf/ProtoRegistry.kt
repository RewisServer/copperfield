package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.ConversionDirection
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoIgnore
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.typeconverter.ZonedDateTimeProtoTypeConverter
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import dev.volix.rewinside.odyssey.common.copperfield.typeconverter.ConvertibleTypeConverter
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : Registry<MessageOrBuilder, ProtoConvertible<*>, ProtoRegistry>(MessageOrBuilder::class.java, ProtoConvertible::class.java) {

    private val setterMethods = mutableMapOf<Field, Method>()
    private val getterMethods = mutableMapOf<Field, Method>()
    private val builderMethods = mutableMapOf<Class<out MessageOrBuilder>, Pair<Method, Method>>()

    init {
        this.setConverter(ZonedDateTime::class.java, ZonedDateTimeProtoTypeConverter(ZoneId.of("Europe/Berlin")))
    }

    override fun <A : MessageOrBuilder> write(entity: ProtoConvertible<*>?, type: Class<A>): A? {
        if (entity == null) return null

        val builderMethods = this.builderMethods.getOrPut(type) {
            val newBuilderMethod = type.getDeclaredMethod("newBuilder")
            val builder = newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>
            val buildMethod = builder.javaClass.getDeclaredMethod("build")
            return@getOrPut newBuilderMethod to buildMethod
        }

        val newBuilderMethod = builderMethods.first
        val builder = newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>
        val buildMethod = builderMethods.second

        this.getFields(entity.javaClass, ConversionDirection.SERIALIZE).forEach { (field, name) ->
            val method = this.setterMethods.getOrPut(field) {
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

                return@getOrPut method
            }

            val converter = this.getConverter(field.type)
            var value = converter.tryConvertOursToTheirs(field.get(entity), field, this)

            if (value is List<*>) {
                value = this.convertOurListToTheirs(value, field)
            }

            method.invoke(builder, value)
        }

        return buildMethod.invoke(builder) as A?
    }

    override fun <A : ProtoConvertible<*>> read(entity: MessageOrBuilder?, type: Class<A>): A? {
        if (entity == null) return null

        val instance = type.newInstance()
        this.getFields(type, ConversionDirection.DESERIALIZE).forEach { (field, name) ->
            val method = this.getterMethods.getOrPut(field) {
                val methodName = if (List::class.java.isAssignableFrom(field.type)) {
                    "get${name.snakeToPascalCase()}List"
                } else {
                    "get${name.snakeToPascalCase()}"
                }

                return@getOrPut entity.javaClass.getDeclaredMethod(methodName)
            }

            val value = method.invoke(entity)

            val converter = this.getConverter(field.type)
            var convertedValue = converter.tryConvertTheirsToOurs(value, field, this)

            if (convertedValue is List<*>) {
                convertedValue = this.convertTheirListToOurs(convertedValue, field)
            }

            field.set(instance, convertedValue)
        }
        return instance
    }

    override fun <A : ProtoConvertible<*>> createConvertibleTypeConverter(type: Class<A>): ConvertibleTypeConverter<MessageOrBuilder, A, *> {
        return ConvertibleTypeConverter<MessageOrBuilder, A, ProtoRegistry>(type, type.newInstance().protoClass)
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
