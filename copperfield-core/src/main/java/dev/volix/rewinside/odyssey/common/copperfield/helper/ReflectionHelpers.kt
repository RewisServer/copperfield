package dev.volix.rewinside.odyssey.common.copperfield

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */

fun getAllFields(type: Class<*>): Collection<Field> {
    val fields = mutableListOf<Field>()
    var currentType: Class<*>? = type
    do {
        fields.addAll(type.declaredFields)
        currentType = currentType?.superclass
    } while (currentType != null)
    return fields
}

fun <T : Annotation> getAnnotation(type: Class<*>, annotationType: Class<out T>): T? {
    var annotation = type.getDeclaredAnnotation(annotationType)
    if (annotation != null) return annotation

    if (type.superclass != null) {
        annotation = getAnnotation(type.superclass, annotationType)
        if (annotation != null) return annotation
    }

    return type.interfaces.mapNotNull { getAnnotation(it, annotationType) }.firstOrNull()
}
