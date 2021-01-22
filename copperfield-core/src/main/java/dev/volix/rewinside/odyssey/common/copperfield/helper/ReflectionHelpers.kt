package dev.volix.rewinside.odyssey.common.copperfield.helper

/**
 * @author Benedikt Wüller
 */

fun <T : Annotation> getAnnotation(type: Class<*>, annotationType: Class<out T>): T? {
    var annotation = type.getDeclaredAnnotation(annotationType)
    if (annotation != null) return annotation

    if (type.superclass != null) {
        annotation = getAnnotation(type.superclass, annotationType)
        if (annotation != null) return annotation
    }

    return type.interfaces.mapNotNull { getAnnotation(it, annotationType) }.firstOrNull()
}
