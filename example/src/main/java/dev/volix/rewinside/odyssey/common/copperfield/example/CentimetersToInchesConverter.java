package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.CopperfieldAgent;
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Benedikt Wüller
 */
public class CentimetersToInchesConverter extends Converter<Double, Double> {

    private static final double CONVERSION_FACTOR = 2.54;

    public CentimetersToInchesConverter() {
        super(Double.class, Double.class);
    }

    @Nullable @Override
    public Double toTheirs(@Nullable final Double value, @NotNull final CopperfieldAgent agent, @NotNull final Class<? extends Double> ourType, @NotNull final Class<Object> targetFormat, @Nullable final Field field) {
        if (value == null) return null;
        return value / CONVERSION_FACTOR;
    }

    @Nullable @Override
    public Double toOurs(@Nullable final Double value, @NotNull final CopperfieldAgent agent, @NotNull final Class<? extends Double> ourType, @NotNull final Class<Object> targetFormat, @Nullable final Field field) {
        if (value == null) return null;
        return value * CONVERSION_FACTOR;
    }
}
