package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.BooleanBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.DateBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.DoubleBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.FloatBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.IntBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.LongBsonConverter
import dev.volix.rewinside.odyssey.common.copperfield.bson.converter.StringBsonConverter
import org.bson.Document
import java.util.Date

/**
 * @author Benedikt Wüller
 */
class BsonRegistry : ConverterRegistry<Document>() {

    // TODO
    //  - ByteString (bytes)
    //  - list
    //  - convertible

    init {
        // Register default converters.
        this.registerConverter(Int::class.javaObjectType, IntBsonConverter())
        this.registerConverter(Long::class.javaObjectType, LongBsonConverter())
        this.registerConverter(Double::class.javaObjectType, DoubleBsonConverter())
        this.registerConverter(Float::class.javaObjectType, FloatBsonConverter())
        this.registerConverter(Boolean::class.javaObjectType, BooleanBsonConverter())
        this.registerConverter(String::class.javaObjectType, StringBsonConverter())
        this.registerConverter(Date::class.javaObjectType, DateBsonConverter())
    }

}
