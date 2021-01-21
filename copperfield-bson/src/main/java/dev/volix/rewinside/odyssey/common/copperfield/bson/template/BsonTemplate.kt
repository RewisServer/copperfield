package dev.volix.rewinside.odyssey.common.copperfield.bson.template

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.BsonObjectIdToStringConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.ByteArrayToBsonBinaryConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.CopperToBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.template.Template
import org.bson.Document
import org.bson.types.ObjectId

/**
 * @author Benedikt Wüller
 */
class BsonTemplate : Template() {

    companion object {
        @JvmField
        val FORMAT = Document::class.java
    }

    init {
        this.with(ByteArray::class.java, ByteArrayToBsonBinaryConverter(), FORMAT)
        this.with(CopperConvertable::class.java, CopperToBsonConverter(), FORMAT)

        // Support for cross conversions.
        this.with(ObjectId::class.java, BsonObjectIdToStringConverter(), Any::class.java)
    }

}
