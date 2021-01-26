package dev.volix.rewinside.odyssey.common.copperfield.exception

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class NoConverterFoundException(field: Field?, contextType: Class<*>, message: String) : CopperFieldException(field, contextType, message)
