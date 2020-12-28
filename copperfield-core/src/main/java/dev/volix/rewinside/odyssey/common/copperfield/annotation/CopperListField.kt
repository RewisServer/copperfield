package dev.volix.rewinside.odyssey.common.copperfield.annotation

import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
annotation class CopperListField(val name: String = "", val innerType: KClass<*>)
