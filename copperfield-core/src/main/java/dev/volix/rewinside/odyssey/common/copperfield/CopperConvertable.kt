package dev.volix.rewinside.odyssey.common.copperfield

/**
 * @author Benedikt Wüller
 */
interface CopperConvertable {

    @JvmDefault
    fun onBeforeOursToTheirs() = Unit

    @JvmDefault
    fun onAfterOursToTheirs() = Unit

    @JvmDefault
    fun onBeforeTheirsToOurs() = Unit

    @JvmDefault
    fun onAfterTheirsToOurs() = Unit

}
