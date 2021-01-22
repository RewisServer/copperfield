package dev.volix.rewinside.odyssey.common.copperfield

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
abstract class CopperTypeMapper<Parent : CopperConvertable, T : CopperConvertable>(private vararg val requiredFields: String) {

    abstract fun mapType(instance: Parent, valueType: Class<out Any>): Class<out T>

    fun requires(field: Field) = this.requiredFields.contains(field.name)

}
