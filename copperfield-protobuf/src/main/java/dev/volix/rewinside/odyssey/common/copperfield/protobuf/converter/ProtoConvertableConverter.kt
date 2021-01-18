package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.common.cache.CacheBuilder
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.Struct
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.AutoConverter
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.exception.CopperFieldException
import dev.volix.rewinside.odyssey.common.copperfield.getOrPut
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoConvertable
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

/**
 * @author Benedikt Wüller
 */
class ProtoConvertableConverter : Converter<ProtoConvertable<*>, MessageOrBuilder>(ProtoConvertable::class.java, MessageOrBuilder::class.java) {

    /**
     * Cached methods used to receive the value from the [MessageOrBuilder].
     */
    private val getterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Pair<String, Class<*>>, Method>()

    /**
     * Cached methods used to provide the value for the [MessageOrBuilder].
     */
    private val setterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Pair<String, Class<*>>, Method>()

    override fun toTheirs(value: ProtoConvertable<*>?, field: Field?, registry: Registry<*, *>, type: Class<out ProtoConvertable<*>>): MessageOrBuilder {
        val instance = createBuilder(type)
        if (value == null) return instance

        (registry as Registry<ProtoConvertable<*>, MessageOrBuilder>).getFieldDefinitions(type).forEach {
            val fieldValue = it.field.get(value)

            val convertedValue = try {
                it.converter.toTheirs(fieldValue, it.field, registry, it.field.type)
            } catch (ex: Exception) {
                throw CopperFieldException(registry, it.field, "Unable to convert our value to theirs.", ex)
            }

            val writeType = when {
                it.converter is AutoConverter && it.isMap -> Map::class.java
                it.converter is AutoConverter && it.isIterable -> Iterable::class.java
                Map::class.java.isAssignableFrom(it.converter.theirType) -> Map::class.java
                Iterable::class.java.isAssignableFrom(it.converter.theirType) -> Iterable::class.java
                else -> convertedValue?.javaClass ?: it.converter.theirType
            }

            this.writeTheirValue(it.name, convertedValue, instance, writeType)
        }

        // Convert the builder to a real message.
        return instance.javaClass.getDeclaredMethod("build").invoke(instance) as MessageOrBuilder
    }

    override fun toOurs(value: MessageOrBuilder?, field: Field?, registry: Registry<*, *>, type: Class<out ProtoConvertable<*>>): ProtoConvertable<*> {
        val instance = type.newInstance()
        if (value == null) return instance

        (registry as Registry<ProtoConvertable<*>, MessageOrBuilder>).getFieldDefinitions(type).forEach {
            val readType = when {
                it.converter is AutoConverter && it.isMap -> Map::class.java
                it.converter is AutoConverter && it.isIterable -> Iterable::class.java
                Map::class.java.isAssignableFrom(it.converter.theirType) -> Map::class.java
                Iterable::class.java.isAssignableFrom(it.converter.theirType) -> Iterable::class.java
                else -> it.converter.theirType
            }

            val fieldValue = this.readTheirValue(it.name, value, readType)

            val convertedValue = try {
                it.converter.toOurs(fieldValue, it.field, registry, it.field.type) ?: return@forEach
            } catch (ex: Exception) {
                throw CopperFieldException(registry, it.field, "Unable to convert their value to ours.", ex)
            }

            it.field.set(instance, convertedValue)
        }

        return instance
    }

    private fun createBuilder(type: Class<out ProtoConvertable<*>>): GeneratedMessageV3.Builder<*> {
        val annotation = type.getDeclaredAnnotation(CopperProtoType::class.java)
            ?: throw IllegalStateException("The annotation @CopperProtoType is required for ProtoConvertable: $type")

        // Create a builder instead of the final instance, because ProtoBuf only supports the builder pattern.
        val protoType = annotation.type.java
        val newBuilderMethod = protoType.getDeclaredMethod("newBuilder")
        return newBuilderMethod.invoke(null) as GeneratedMessageV3.Builder<*>
    }

    private fun readTheirValue(name: String, entity: MessageOrBuilder, type: Class<out Any>): Any? {
        return this.getGetterMethod(name, entity.javaClass, type).invoke(entity)
    }

    private fun writeTheirValue(name: String, value: Any?, entity: MessageOrBuilder, type: Class<out Any>) {
        val correctedType = if (type == Map::class.java && value is Struct) Struct::class.java else type
        this.getSetterMethod(name, entity.javaClass, correctedType).invoke(entity, value)
    }

    /**
     * Finds and returns the method used to retrieve a value of [type] with the given [name] from the [entityType].
     * Caches the method for further use.
     */
    private fun getGetterMethod(name: String, entityType: Class<out Any>, type: Class<out Any>): Method {
        val key = name to type
        return this.getterMethods.getOrPut(key) {
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
    private fun getSetterMethod(name: String, entityType: Class<out MessageOrBuilder>, type: Class<out Any>): Method {
        val key = name to type
        return this.setterMethods.getOrPut(key) {
            val methodName = when {
                Iterable::class.java.isAssignableFrom(type) -> "addAll${name.snakeToPascalCase()}"
                else -> "set${name.snakeToPascalCase()}"
            }

            return@getOrPut entityType.declaredMethods.firstOrNull { method ->
                if (method.name != methodName) return@firstOrNull false
                if (method.parameterCount != 1) return@firstOrNull false
                method.parameterTypes.first().isAssignableFrom(type)
            } ?: throw RuntimeException("Unable to find setter method for $type.")
        }
    }

}
