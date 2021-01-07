package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import org.bson.types.ObjectId
import java.lang.reflect.Field

/**
 * Converts bson [ObjectId]s to/from their hex string representation.
 *
 * @author Benedikt Wüller
 */
open class ObjectIdToStringConverter : Converter<ObjectId, String>(ObjectId::class.java, String::class.java) {

    override fun toTheirs(value: ObjectId?, field: Field?, registry: Registry<*, *>, type: Class<out ObjectId>): String? {
        return value?.toHexString()
    }

    override fun toOurs(value: String?, field: Field?, registry: Registry<*, *>, type: Class<out ObjectId>): ObjectId? {
        return if (value == null) null else ObjectId(value)
    }

}
