package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * Internal helper class to obtain and provide name and converter fields of CopperField annotations.
 * This is required because a [Registry] can support multiple annotations with the same signature
 * but annotations do not support inheritance.
 *
 * @author Benedikt Wüller
 */
internal data class CopperFieldAnnotationHelper(val type: Class<out Annotation>) {

    private val nameMethod = this.type.getDeclaredMethod("name")
    private val converterMethod = this.type.getDeclaredMethod("converter")

    fun getName(annotation: Annotation) = this.nameMethod.invoke(annotation) as String

    fun getConverter(annotation: Annotation) = this.converterMethod.invoke(annotation) as Class<Converter<Any, Any>>

}
