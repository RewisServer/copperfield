package dev.volix.rewinside.odyssey.common.copperfield

import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter

/**
 * @author Benedikt Wüller
 */
class CopperFieldAnnotationHelper(val type: Class<out Annotation>) {

    private val nameMethod = this.type.getDeclaredMethod("name")
    private val converterMethod = this.type.getDeclaredMethod("converter")

    fun getName(annotation: Annotation) = this.nameMethod.invoke(annotation) as String

    fun getConverter(annotation: Annotation) = this.converterMethod.invoke(annotation) as Class<Converter<Any, Any>>

}
