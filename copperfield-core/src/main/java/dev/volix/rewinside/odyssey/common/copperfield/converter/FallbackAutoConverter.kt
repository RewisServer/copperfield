package dev.volix.rewinside.odyssey.common.copperfield.converter

/**
 * This converter is used to tell the registry to use the [Converter] defined in the
 * [dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField] annotation
 * or use the parent [AutoConverter] if there is no annotation.
 *
 * @author Benedikt Wüller
 */
@Deprecated(message = "This converter should never be assigned to any CopperField manually.")
class FallbackAutoConverter : AutoConverter()
