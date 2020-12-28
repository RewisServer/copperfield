package dev.volix.rewinside.odyssey.common.copperfield.exception

/**
 * @author Benedikt Wüller
 */
class PrimitiveTypeException(name: String, type: Class<*>) : RuntimeException("Primitive type ($type) are not supported for the CopperField ($name)." +
        "Use boxed or object types instead because all fields may be null."
)
