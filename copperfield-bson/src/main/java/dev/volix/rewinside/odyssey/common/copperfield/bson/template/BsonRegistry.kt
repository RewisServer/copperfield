package dev.volix.rewinside.odyssey.common.copperfield.bson.template

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.BsonObjectIdToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.CopperToBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.template.Registry
import org.bson.Document
import org.bson.types.ObjectId

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : Registry() {

    companion object {
        @JvmField
        val TARGET_BSON = Document::class.java
    }

    init {
        this.with(ByteArray::class.java, ByteArrayToBsonBinaryConverter::class.java, TARGET_BSON)
        this.with(CopperConvertable::class.java, CopperToBsonConverter::class.java, TARGET_BSON)

        // Support for cross conversions.
        this.with(ObjectId::class.java, BsonObjectIdToStringConverter::class.java, Any::class.java)
    }

}
