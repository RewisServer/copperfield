package dev.volix.rewinside.odyssey.common.copperfield.proto.converter

import com.google.common.cache.CacheBuilder
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageLiteOrBuilder
import com.google.protobuf.Struct
import com.google.protobuf.StructOrBuilder
import com.google.protobuf.Value
import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.TypeMapper
import dev.volix.rewinside.odyssey.common.copperfield.convertFromValue
import dev.volix.rewinside.odyssey.common.copperfield.convertToValue
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.CopperConvertableConverter
import dev.volix.rewinside.odyssey.common.copperfield.getAnnotation
import dev.volix.rewinside.odyssey.common.copperfield.getBaseValueType
import dev.volix.rewinside.odyssey.common.copperfield.getOrPut
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoClass
import dev.volix.rewinside.odyssey.common.copperfield.proto.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

/**
 * @author Benedikt Wüller
 */
class CopperToProtoConverter : CopperConvertableConverter<MessageLiteOrBuilder>(MessageLiteOrBuilder::class.java) {

    /**
     * Cached methods used to receive the value from the [MessageLiteOrBuilder].
     */
    private val getterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Triple<String, Class<*>, Class<*>>, Method>()

    /**
     * Cached methods used to receive the value from the [MessageLiteOrBuilder].
     */
    private val hasMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Triple<String, Class<*>, Class<*>>, Method?>()

    /**
     * Cached methods used to provide the value for the [MessageLiteOrBuilder].
     */
    private val setterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Triple<String, Class<*>, Class<*>>, Method>()

    override fun createTheirInstance(type: Class<out MessageLiteOrBuilder>, ourType: Class<out CopperConvertable>?): MessageLiteOrBuilder {
        val annotation = if (ourType == null) null else getAnnotation(ourType, CopperProtoClass::class.java)
        val protoType = annotation?.type?.java ?: type

        // Create a builder instead of the final instance, because ProtoBuf only supports the builder pattern.
        val newBuilderMethod = protoType.getDeclaredMethod("newBuilder")
        return newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>
    }

    override fun finalizeTheirInstance(instance: MessageLiteOrBuilder): MessageLiteOrBuilder {
        return instance.javaClass.getDeclaredMethod("build").invoke(instance) as MessageLiteOrBuilder
    }

    override fun getValue(instance: MessageLiteOrBuilder, name: String, type: Class<*>): Any? {
        val exists = this.getHasMethod(name, instance.javaClass, type)
        val getter = this.getGetterMethod(name, instance.javaClass, type)

        if (instance is StructOrBuilder) {
            return convertFromValue(getter.invoke(instance, name, null) as Value?)
        } else {
            if (exists == null || exists.invoke(instance) == true) {
                return getter.invoke(instance)
            }
            return null
        }
    }

    override fun setValue(instance: MessageLiteOrBuilder, name: String, value: Any?, type: Class<*>) {
        val correctedType = if (type == Map::class.java && value is Struct) Struct::class.java else type
        val method = this.getSetterMethod(name, instance.javaClass, correctedType)

        if (instance is StructOrBuilder) {
            method.invoke(instance, name, convertToValue(value))
        } else {
            val valueType = getBaseValueType(type)
            if (valueType != null && method.parameters.first().type.isAssignableFrom(valueType)) {
                method.invoke(instance, convertToValue(value))
            } else {
                method.invoke(instance, value)
            }
        }
    }

    /**
     * Finds and returns the method used to retrieve a value of [type] with the given [name] from the [entityType].
     * Caches the method for further use.
     */
    private fun getHasMethod(name: String, entityType: Class<out Any>, type: Class<out Any>): Method? {
        val key = Triple(name, type, entityType)
        return this.hasMethods.getOrPut(key) {
            return@getOrPut try {
                entityType.getDeclaredMethod("has${name.snakeToPascalCase()}")
            } catch (ex: Exception) {
                null
            }
        }
    }

    /**
     * Finds and returns the method used to retrieve a value of [type] with the given [name] from the [entityType].
     * Caches the method for further use.
     */
    private fun getGetterMethod(name: String, entityType: Class<out Any>, type: Class<out Any>): Method {
        val key = Triple(name, type, entityType)
        return this.getterMethods.getOrPut(key) {
            if (StructOrBuilder::class.java.isAssignableFrom(entityType)) {
                return@getOrPut entityType.getDeclaredMethod("getFieldsOrDefault", String::class.java, Value::class.java)
            }

            val methodName = when {
                Iterable::class.java.isAssignableFrom(type) -> "get${name.snakeToPascalCase()}List"
                else -> "get${name.snakeToPascalCase()}"
            }

            return@getOrPut entityType.getDeclaredMethod(methodName)
        }
    }

    /**
     * Finds and returns the method used to provide a value of [type] with the given [name] for the [entityType].
     * Caches the method for further use.
     */
    private fun getSetterMethod(name: String, entityType: Class<out MessageLiteOrBuilder>, type: Class<out Any>): Method {
        val key = Triple(name, type, entityType)
        return this.setterMethods.getOrPut(key) {
            if (StructOrBuilder::class.java.isAssignableFrom(entityType)) {
                return@getOrPut entityType.getDeclaredMethod("putFields", String::class.java, Value::class.java)
            }

            val methodName = when {
                Iterable::class.java.isAssignableFrom(type) -> "addAll${name.snakeToPascalCase()}"
                else -> "set${name.snakeToPascalCase()}"
            }

            val valueType = getBaseValueType(type)
            return@getOrPut entityType.declaredMethods.firstOrNull { method ->
                if (method.name != methodName) return@firstOrNull false
                if (method.parameterCount != 1) return@firstOrNull false
                val parameter = method.parameterTypes.first()
                parameter.isAssignableFrom(type) || (valueType != null && parameter.isAssignableFrom(valueType))
            } ?: throw RuntimeException("Unable to find setter method for $type.")
        }
    }

    override fun getName(name: String, field: Field): String {
        val annotation = field.getDeclaredAnnotation(CopperProtoField::class.java)
        if (annotation != null && annotation.name.isNotEmpty()) return annotation.name
        return super.getName(name, field)
    }

    override fun getConverterType(type: Class<Converter<Any, Any>>, field: Field): Class<Converter<Any, Any>> {
        val annotation = field.getDeclaredAnnotation(CopperProtoField::class.java)
        if (annotation != null && annotation.converter != Converter::class) return annotation.converter.java as Class<Converter<Any, Any>>
        return super.getConverterType(type, field)
    }

    override fun getTypeMapper(type: Class<TypeMapper<out CopperConvertable, CopperConvertable>>, field: Field): Class<TypeMapper<out CopperConvertable, CopperConvertable>> {
        val annotation = field.getDeclaredAnnotation(CopperProtoField::class.java)
        if (annotation != null && annotation.typeMapper != TypeMapper::class) return annotation.typeMapper.java as Class<TypeMapper<out CopperConvertable, CopperConvertable>>
        return super.getTypeMapper(type, field)
    }

}
