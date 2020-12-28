package dev.volix.rewinside.odyssey.common.copperfield.protobuf

import com.google.protobuf.GeneratedMessageV3
import dev.volix.rewinside.odyssey.common.copperfield.ReflectionConverter

/**
 * @author Benedikt Wüller
 */
open class ProtoReflectionConverter<T : Any>(type: Class<T>) : ReflectionConverter<GeneratedMessageV3.Builder<*>, T>(type), ProtoConverter<T> {

    override fun getGetterMethodName(name: String) = "get${name.capitalize()}"

    override fun getSetterMethodName(name: String) = "set${name.capitalize()}"

}
