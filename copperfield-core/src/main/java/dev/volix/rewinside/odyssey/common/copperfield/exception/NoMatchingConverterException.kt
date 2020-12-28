package dev.volix.rewinside.odyssey.common.copperfield.exception

/**
 * @author Benedikt Wüller
 */
class NoMatchingConverterException(name: String, convertibleType: Class<*>)
    : RuntimeException("No converter found for field $name in convertible $convertibleType. " +
        "Consider registering a custom converter to the registry or using a different type."
)
