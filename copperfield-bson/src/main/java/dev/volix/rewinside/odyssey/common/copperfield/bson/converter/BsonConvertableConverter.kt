package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonConvertable
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.exception.CopperFieldException
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class BsonConvertableConverter : Converter<BsonConvertable, Document>(BsonConvertable::class.java, Document::class.java) {

    override fun toTheirs(value: BsonConvertable?, field: Field?, registry: Registry<*, *>, type: Class<out BsonConvertable>): Document {
        val document = Document()

        (registry as Registry<BsonConvertable, Document>).getFieldDefinitions(type).forEach {
            val fieldValue = it.field.get(value)

            val convertedValue = try {
                it.converter.toTheirs(fieldValue, it.field, registry, it.field.type)
            } catch (ex: Exception) {
                throw CopperFieldException(registry, it.field, "Unable to convert our value to theirs.", ex)
            }

            val writeType = when {
                it.isMap -> Map::class.java
                it.isIterable -> Iterable::class.java
                else -> convertedValue?.javaClass ?: it.converter.theirType
            }

            this.writeTheirValue(it.name, convertedValue, document)
        }

        return document
    }

    override fun toOurs(value: Document?, field: Field?, registry: Registry<*, *>, type: Class<out BsonConvertable>): BsonConvertable? {
        val instance = type.newInstance()
        if (value == null) return instance

        (registry as Registry<BsonConvertable, Document>).getFieldDefinitions(type).forEach {
            val readType = when {
                it.isMap -> Map::class.java
                it.isIterable -> Iterable::class.java
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

    private fun readTheirValue(name: String, entity: Document, type: Class<out Any>): Any? {
        val splits = name.split(".")
        var document = entity

        if (splits.size > 1) {
            for (i in 0 until splits.size - 1) {
                val part = splits[i]
                document = entity.get(part, Document())
            }
        }

        return document[splits.last(), type]
    }

    private fun writeTheirValue(name: String, value: Any?, entity: Document) {
        val splits = name.split(".")
        var document = entity

        if (splits.size > 1) {
            for (i in 0 until splits.size - 1) {
                val part = splits[i]
                if (!entity.containsKey(part)) entity[part] = Document()
                document = entity.get(part, Document::class.java)
            }
        }

        document[splits.last()] = value
    }

}
