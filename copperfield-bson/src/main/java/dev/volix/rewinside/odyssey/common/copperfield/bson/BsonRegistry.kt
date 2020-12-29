package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonIgnore
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonName
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : Registry<BsonConvertable, Document>(BsonConvertable::class.java, Document::class.java) {

    init {
        this.setConverter(ByteArray::class.java, ByteArrayToBsonBinaryConverter())
    }

    override fun createTheirs(convertible: BsonConvertable) = Document()

    override fun readValue(name: String, entity: Document, type: Class<out Any>): Any? {
        return entity[name, type]
    }

    override fun writeValue(name: String, value: Any?, entity: Document, type: Class<out Any>) {
        entity[name] = value
    }

    override fun getName(name: String, field: Field): String {
        return field.getDeclaredAnnotation(CopperBsonName::class.java)?.name ?: super.getName(name, field)
    }

    override fun isIgnored(field: Field) = super.isIgnored(field) || field.isAnnotationPresent(CopperBsonIgnore::class.java)

}
