package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import org.bson.Document

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : Registry<BsonConvertable, Document>(BsonConvertable::class.java, Document::class.java) {

    init {
        this.registerAnnotation(CopperBsonField::class.java)
        this.setConverter(ByteArray::class.java, ByteArrayToBsonBinaryConverter())
    }

    override fun createTheirs(convertible: BsonConvertable) = Document()

    override fun readValue(name: String, entity: Document, type: Class<out Any>): Any? {
        return entity[name, type]
    }

    override fun writeValue(name: String, value: Any?, entity: Document, type: Class<out Any>) {
        entity[name] = value
    }

}
