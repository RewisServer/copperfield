package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import org.bson.types.ObjectId
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class BsonObjectIdToStringConverter : Converter<ObjectId, String>(ObjectId::class.java, String::class.java) {

    override fun toTheirs(value: ObjectId?, registry: Registry, ourType: Class<out ObjectId>, targetFormatType: Class<*>, field: Field?): String? {
        return value?.toHexString()
    }

    override fun toOurs(value: String?, registry: Registry, ourType: Class<out ObjectId>, targetFormatType: Class<*>, field: Field?): ObjectId? {
        return if (value == null) null else ObjectId(value)
    }

}
