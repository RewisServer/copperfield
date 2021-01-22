package dev.volix.rewinside.odyssey.common.copperfield.bson.registry

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.BsonObjectIdToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.CopperToBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.registry.Registry
import org.bson.Document
import org.bson.types.ObjectId

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : Registry() {

    companion object {
        @JvmField
        val BSON = Document::class.java
    }

    init {
        this.with(ByteArray::class.java, ByteArrayToBsonBinaryConverter::class.java, BSON)
        this.with(CopperConvertable::class.java, CopperToBsonConverter::class.java, BSON)

        // Support for cross conversions.
        this.with(ObjectId::class.java, BsonObjectIdToStringConverter::class.java, Any::class.java)
    }

}
