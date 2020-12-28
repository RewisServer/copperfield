package dev.volix.rewinside.odyssey.common.copperfield.bson.typeconverter

import dev.volix.rewinside.odyssey.common.copperfield.bson.BsonRegistry
import org.bson.types.Binary
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class ByteArrayBsonTypeConverter
    : BsonTypeConverter<ByteArray, Binary>(ByteArray::class.java, Binary::class.java) {

    override fun convertOursToTheirs(value: ByteArray, field: Field, registry: BsonRegistry) = Binary(value)

    override fun convertTheirsToOurs(value: Binary, field: Field, registry: BsonRegistry) = value.data

}
