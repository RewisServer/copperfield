package dev.volix.rewinside.odyssey.common.copperfield.bson;

import dev.volix.rewinside.odyssey.common.copperfield.CopperField;
import org.bson.Document;

/**
 * @author Benedikt Wüller
 */
public class Test implements BsonConvertible {

    @CopperField(name = "test")
    public Long test;

    @CopperField(name = "foo")
    public String foo;

    @CopperField(name = "value")
    public Double value;

    public static void main(String[] args) {
        final BsonRegistry registry = new BsonRegistry();

        System.out.println(new Test().toBsonDocument(registry).toJson());

        final Test instance = new Test();
        instance.test = 1234L;
        instance.foo = "bar";
        instance.value = 4.20;

        final Document document = instance.toBsonDocument(registry);
        System.out.println(document.toJson());

        final Test anotherInstance = new Test();
        anotherInstance.fromBsonDocument(document, registry);

        final String foo = "bar";
    }

}
