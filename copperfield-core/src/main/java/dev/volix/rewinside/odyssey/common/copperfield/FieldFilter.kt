package dev.volix.rewinside.odyssey.common.copperfield

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class FieldFilter<T : Annotation>(private val type: Class<T>) {

    fun filterSerialize(field: Field): Boolean {
        if (!field.isAnnotationPresent(this.type)) return true
        return !this.shouldIgnoreOnSerialize(field.getDeclaredAnnotation(this.type))
    }

    fun filterDeserialize(field: Field): Boolean {
        if (!field.isAnnotationPresent(this.type)) return true
        return !this.shouldIgnoreOnDeserialize(field.getDeclaredAnnotation(this.type))
    }

    protected abstract fun shouldIgnoreOnSerialize(annotation: T): Boolean

    protected abstract fun shouldIgnoreOnDeserialize(annotation: T): Boolean

}
