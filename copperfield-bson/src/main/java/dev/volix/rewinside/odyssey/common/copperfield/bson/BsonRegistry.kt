package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.ConversionDirection
import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonIgnore
import dev.volix.rewinside.odyssey.common.copperfield.bson.typeconverter.ByteArrayBsonTypeConverter
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : Registry<Document, BsonConvertible, BsonRegistry>(Document::class.java, BsonConvertible::class.java) {

    // TODO
    //  - list entry conversion

    init {
//        this.defaultConverter = SimpleBsonConverter()
//        this.registerConverter(Number::class.java, NumberBsonConverter())
//        this.registerConverter(UUID::class.java, UuidBsonConverter())
//        this.registerConverter(ByteArray::class.java, ByteArrayBsonConverter())
//        this.registerConverter(BsonConvertible::class.java, ConvertibleBsonConverter())

        // ------------------------------------------------------------------------------

        this.setConverter(ByteArray::class.java, ByteArrayBsonTypeConverter())
    }

    override fun <T : Document> write(entity: BsonConvertible?, type: Class<T>): T? {
        if (entity == null) return null
        val document = Document()
        this.getFields(entity.javaClass, ConversionDirection.SERIALIZE).forEach { (field, name) ->
            val converter = this.getConverter(field.type)
            val value = converter.convertOursToTheirs(field.get(entity), field, this)
            document[name] = value
        }
        return document as T?
    }

    override fun <T : BsonConvertible> read(entity: Document?, type: Class<T>): T? {
        if (entity == null) return null
        val instance = type.newInstance()
        this.getFields(type, ConversionDirection.DESERIALIZE).forEach { (field, name) ->
            val converter = this.getConverter(field.type)
            val value = entity.get(name, converter.theirType)
            val convertedValue = converter.convertTheirsToOurs(value, field, this)
            field.set(instance, convertedValue)
        }
        return instance
    }

    override fun isIgnored(field: Field, direction: ConversionDirection): Boolean {
        val annotation = field.getDeclaredAnnotation(CopperBsonIgnore::class.java) ?: return false
        return when (direction) {
            ConversionDirection.SERIALIZE -> annotation.ignoreSerialize
            ConversionDirection.DESERIALIZE -> annotation.ignoreDeserialize
            else -> false
        }
    }

    override fun isAnnotated(field: Field) = super.isAnnotated(field) || field.isAnnotationPresent(CopperBsonField::class.java)

    override fun getName(field: Field): String? {
        var name = field.getDeclaredAnnotation(CopperBsonField::class.java)?.name
        if (name.isNullOrEmpty()) {
            name = super.getName(field)
        }
        return name
    }

}
