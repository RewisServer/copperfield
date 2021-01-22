package dev.volix.rewinside.odyssey.common.copperfield.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flags the field to be ignored by the conversion process of
 * {@link dev.volix.rewinside.odyssey.common.copperfield.converter.CopperConvertableConverter}s.
 *
 * The {@link #contextTypes()} define all context types for which this field will be ignored. By default, this field will be ignored for all context
 * types.
 *
 * @see dev.volix.rewinside.odyssey.common.copperfield.converter.CopperConvertableConverter
 *
 * @author Benedikt Wüller
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CopperIgnore {
    Class[] contextTypes() default {Object.class };
}
