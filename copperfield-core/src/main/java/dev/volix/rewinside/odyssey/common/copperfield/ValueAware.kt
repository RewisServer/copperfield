package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperValueType
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
interface ValueAware {

    @JvmDefault
    fun getValueType(field: Field?): Class<*> {
        val annotation = field?.getDeclaredAnnotation(CopperValueType::class.java)
        return annotation?.type?.java ?: Any::class.java
    }

}
