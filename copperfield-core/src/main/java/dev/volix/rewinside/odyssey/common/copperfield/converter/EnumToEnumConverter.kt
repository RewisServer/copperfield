package dev.volix.rewinside.odyssey.common.copperfield.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent
import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperEnumField
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class EnumToEnumConverter : Converter<Enum<*>, Enum<*>>(Enum::class.java, Enum::class.java) {

    override fun toTheirs(value: Enum<*>?, agent: CopperfieldAgent, ourType: Class<out Enum<*>>, targetFormat: Class<Any>, field: Field?): Enum<*>? {
        if (value == null) return null
        val theirType = this.getEnumClass(field)
        val method = theirType.getDeclaredMethod("valueOf", String::class.java)
        return method.invoke(null, value) as Enum<*>
    }

    override fun toOurs(value: Enum<*>?, agent: CopperfieldAgent, ourType: Class<out Enum<*>>, targetFormat: Class<Any>, field: Field?): Enum<*>? {
        if (value == null) return null
        val method = ourType.getDeclaredMethod("valueOf", String::class.java)
        return method.invoke(null, value) as Enum<*>
    }

    private fun getEnumClass(field: Field?): Class<out Enum<*>> {
        val annotation = field?.getDeclaredAnnotation(CopperEnumField::class.java) ?: throw IllegalStateException("The annotation @CopperEnumField is missing.")
        return annotation.targetEnumType.java
    }

}
