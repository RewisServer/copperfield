package dev.volix.rewinside.odyssey.common.copperfield

import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
interface TypeMapper<Parent, T> {

    fun map(instance: Parent, valueType: Class<out Any>): Class<out T>

    fun getRequiredFieldNames(): Array<String>

    @JvmDefault
    fun requires(field: Field) = this.getRequiredFieldNames().contains(field.name)

}
