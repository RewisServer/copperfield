package dev.volix.rewinside.odyssey.common.copperfield

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class FieldNameMapper<T : Annotation>(private val type: Class<T>) {

    fun map(field: Field, defaultName: String): String {
        if (!field.isAnnotationPresent(this.type)) return defaultName
        return this.getName(field.getDeclaredAnnotation(this.type))
    }

    protected abstract fun getName(annotation: T): String

}
