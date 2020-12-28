package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import org.bson.Document
import java.util.Date

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : ConverterRegistry<Document>() {

    // TODO
    //  - ByteString (bytes)
    //  - list

    init {
        // Register default converters.
        this.registerConverter(Int::class.java, IntBsonConverter())
        this.registerConverter(Long::class.java, LongBsonConverter())
        this.registerConverter(Double::class.java, DoubleBsonConverter())
        this.registerConverter(Float::class.java, FloatBsonConverter())
        this.registerConverter(Boolean::class.java, BooleanBsonConverter())
        this.registerConverter(String::class.java, StringBsonConverter())
        this.registerConverter(Date::class.java, DateBsonConverter())
    }

}
