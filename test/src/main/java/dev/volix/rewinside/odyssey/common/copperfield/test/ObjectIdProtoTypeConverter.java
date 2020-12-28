package dev.volix.rewinside.odyssey.common.copperfield.test;

import dev.volix.rewinside.odyssey.common.copperfield.protobuf.ProtoRegistry;
import dev.volix.rewinside.odyssey.common.copperfield.protobuf.typeconverter.ProtoTypeConverter;
import java.lang.reflect.Field;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * @author Benedikt Wüller
 */
public class ObjectIdProtoTypeConverter extends ProtoTypeConverter<ObjectId, String> {

    public ObjectIdProtoTypeConverter() {
        super(ObjectId.class, String.class);
    }

    @Override
    public String convertOursToTheirs(@NotNull final ObjectId value, @NotNull final Field field, @NotNull final ProtoRegistry registry) {
        return value.toHexString();
    }

    @Override
    public ObjectId convertTheirsToOurs(@NotNull final String value, @NotNull final Field field, @NotNull final ProtoRegistry registry) {
        return new ObjectId(value);
    }

}
