package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Converter
import org.bson.Document


/**
 * @author Benedikt Wüller
 */
interface BsonConverter<T : Any> : Converter<Document, T> {

    // TODO
    //  - float
    //  - int
    //  - ByteString (Binary)

    override fun convertTo(name: String, value: T?, target: Document) {
        target[name] = value
    }

}
