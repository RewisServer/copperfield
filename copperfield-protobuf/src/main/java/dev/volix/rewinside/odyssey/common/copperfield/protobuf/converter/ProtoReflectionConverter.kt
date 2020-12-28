package dev.volix.rewinside.odyssey.common.copperfield.protobuf.converter

import com.google.protobuf.MessageOrBuilder
import dev.volix.rewinside.odyssey.common.copperfield.converter.ReflectionConverter
import dev.volix.rewinside.odyssey.common.copperfield.snakeToPascalCase

/**
 * @author Benedikt Wüller
 */
open class ProtoReflectionConverter<T : Any> : ReflectionConverter<MessageOrBuilder, T>(), ProtoConverter<T> {

    override fun getGetterMethodName(name: String) = "get${name.snakeToPascalCase()}"

    override fun getSetterMethodName(name: String) = "set${name.snakeToPascalCase()}"

}
