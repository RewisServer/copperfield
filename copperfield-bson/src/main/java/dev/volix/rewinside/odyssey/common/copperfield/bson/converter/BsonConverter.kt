package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.ConverterRegistry
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import org.bson.Document
import java.lang.reflect.Field


/**
 * @author Benedikt Wüller
 */
interface BsonConverter<T : Any> : Converter<Document, T> {

    // TODO
    //  - float
    //  - int
    //  - ByteString (Binary)

    override fun convertTo(name: String, value: T?, target: Document, field: Field, registry: ConverterRegistry<Document>) {
        target[name] = value
    }

}
