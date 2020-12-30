# copperfield

Copperfield provides a series of annotations to convert POJOs both to and from multiple different external formats.

[[_TOC_]]

## Supported formats

* MongoDB (`bson`)
* Protocol Buffers (`proto3`)

## Usage

For each target format there is a `Registry` and `Convertable` implementation. For example: the `BsonRegistry` only supports conversion
of classes implementing the `BsonConvertable` interface.

_**Note**: For the conversion to work as expected, there **must** be either no constructor or at least one without arguments on the POJO of choice._

In addition, each field to be converted has to be annotated using `@CopperField`. This annotation defines the `name` of the field in
the target format(s) as well as the `converter` used to modify this fields value in order to fit the required type in the target format(s).
If no converter is defined, the default `AutoConverter` is being used. This will use the individual converters registered in the registry
based on the field types.

The format specific `@Copper<Type>Field` annotations can either be used as standalone annotations or in combination with `@CopperField`.
When using them as standalone annotations, this field will only be converted to this specific format.
When using them additionally, the `name` and `converter` values will override those of the `@CopperField` annotation for this format, if they are set.

When working with maps and/or iterables and their respective default converters, the `@CopperMapTypes` and `@CopperIterableType` annotations **must**
be set as well. Those are required to ensure the corresponding converters are being used on the keys and/or values. Additional converters can
also be set.

If you want to ignore a field for one or more target formats, use the `@CopperIgnore` annotation.

```java
@CopperProtoType(type = ProtoPerson.class) // required for ProtoConvertables.
public class Person implements BsonConvertable, ProtoConvertable<ProtoPerson> {

    @CopperField(name = "id") // define the default name for this field.
    @CopperBsonField(name = "uuid") // use a different name for bson conversion.
    public UUID uuid;

    @CopperField(name = "name")
    public String name;

    @CopperField(name = "created_at")
    @CopperIgnore(convertables = { BsonConvertable.class }) // ignore field for bson conversion.
    public ZonedDateTime createdAt;

    @CopperField(name = "has_traits")
    @CopperMapTypes( // required to correctly convert the external format back to ours.
            keyType = TraitType.class,
            valueType = Boolean.class,
            keyConverter = EnumToStringConverter.class // optional converter to use for keys only.
    )
    public Map<TraitType, Boolean> traits = new HashMap<>();
    
    @CopperBsonField(name = "job_history") // only convert to bson format.
    @CopperIterableType(innerType = JobEntry.class) // required to correctly convert the external format back to ours.
    public List<JobEntry> jobHistory = new ArrayList<>();

}
```

```java
final Person person = //...

// Conversion to and from bson document
final BsonRegistry bsonRegistry = new BsonRegistry();
final Document document = bsonRegistry.toTheirs(person);
final Person bsonPerson = bsonRegistry.toOurs(document, Person.class);

// Conversion to and from proto message
final ProtoRegistry protoRegistry = new ProtoRegistry();
final ProtoPerson message = protoRegistry.toTheirs(person);
final Person protoPerson = protoRegistry.toOurs(message, Person.class);
```

Custom converters can be registered or replaced using `registry.setConverter(valueType, converter)`. 
