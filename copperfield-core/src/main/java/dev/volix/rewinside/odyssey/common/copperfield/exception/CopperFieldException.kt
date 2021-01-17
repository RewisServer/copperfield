package dev.volix.rewinside.odyssey.common.copperfield.exception

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
open class CopperFieldException(registry: Registry<*, *>, field: Field, throwable: Throwable)
    : RuntimeException("An error occurred for field ${field.name} in ${field.declaringClass.name} using registry ${registry.javaClass.name}.", throwable) {

    constructor(registry: Registry<*, *>, field: Field, message: String) : this(registry, field, RuntimeException(message))

    constructor(registry: Registry<*, *>, field: Field, message: String, throwable: Throwable) : this(registry, field, RuntimeException(message, throwable))

}


