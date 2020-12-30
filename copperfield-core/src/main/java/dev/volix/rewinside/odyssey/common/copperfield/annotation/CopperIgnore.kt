package dev.volix.rewinside.odyssey.common.copperfield.annotation

import dev.volix.rewinside.odyssey.common.copperfield.Convertable
import kotlin.reflect.KClass

/**
 * @author Benedikt Wüller
 */
annotation class CopperIgnore(vararg val convertables: KClass<out Convertable>)
