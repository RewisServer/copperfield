package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.common.cache.CacheBuilder
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.Struct
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.clear
import dev.volix.rewinside.odyssey.common.copperfield.getOrPut
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoField
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.annotation.CopperProtoType
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ByteArrayToByteStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.MapToProtoStructConverter
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter.ZonedDateTimeToProtoTimestampConverter
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase
import java.lang.reflect.Method
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * @author Benedikt Wüller
 */
class ProtoRegistry : Registry<ProtoConvertable<*>, MessageOrBuilder>(ProtoConvertable::class.java, MessageOrBuilder::class.java) {

    private val getterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Pair<String, Class<*>>, Method>()

    private val setterMethods = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build<Pair<String, Class<*>>, Method>()

    init {
        // Register additional annotation.
        this.registerAnnotation(CopperProtoField::class.java)

        // Register additional/replacement converters.
        this.setConverter(Map::class.java, MapToProtoStructConverter::class.java)
        this.setConverter(ByteArray::class.java, ByteArrayToByteStringConverter::class.java)
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
        return this.getGetterMethod(name, entity.javaClass, type).invoke(entity)
    }

    override fun writeValue(name: String, value: Any?, entity: MessageOrBuilder, type: Class<out Any>) {
        val correctedType = if (type == Map::class.java && value is Struct) Struct::class.java else type
        this.getSetterMethod(name, entity.javaClass, correctedType).invoke(entity, value)
    }

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

    override fun clearCache() {
        super.clearCache()
        this.getterMethods.clear()
        this.setterMethods.clear()
    }

}
