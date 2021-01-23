# copperfield

A flexible, lightweight library to do ORMs using annotations (and magic).

[[_TOC_]]

## Getting Started



TODO
* Maven dependency
* Bson dependency
* Proto dependency

## Usage

### Value Conversion

At it's core, copperfield is a library to convert values of one datatype to values of another datatype defined in a so-called `Converter` and vice
versa.

```java
Converter<UUID, String> converter = new UuidToStringConverter();
converter.toTheirs(/* ... */); // Converts UUIDs to Strings
converter.toOurs(/* ... */); // Converts Strings to UUIDs
```

However, `Converters` require a bunch of parameters in order to be able to convert the values correctly in all circumstances. Because they are quite
delicate, they should not be set manually. This is where the `CopperfieldAgent` comes in. The agent provides a bunch of accessibility methods to
convert values.

```java
CopperfieldAgent agent = new CopperfieldAgent();
String uuidAsString = (String) agent.toTheirs(UUID.randomUUID());
UUID stringAsUuid = agent.toOurs(uuidAsString, UUID.class); 
```

The agent internally selects the converter matching the type, or the classes supertype, of the given value. The following converters are available by
default. All converters support conversion in both directions by default, however there are some exceptions.

| Our Type | Their Type | Converter | Comment |
| ---------- | ----------- | --------- | ------- |
| `UUID` | `String` | `UuidToStringConverter` | Converts uuids to strings using `toString()`. |
| `Enum` | `String` | `EnumToStringConverter` | Converts enums to their corresponding names. |
| `OffsetDateTime` | `String` | `OffsetDateTimeToStringConverter` | Converts datetime instances to ISO 8601 strings. |
| `Number` | `Number` | `NumberConverter` | Makes sure that the number type does not get lost when converting `toOurs()`. |
| `Iterable` | `Iterable` | `IterableConverter` | Converts all values using the matching converters defined for the `CopperfieldAgent`. This will only work when converting `toTheirs` by default. See [POJO Conversion](#pojo-conversion). |
| `Map` | `Map` | `MapConverter` | Converts all keys and values using the matching converters defined for the `CopperfieldAgent`. This will only work when converting `toTheirs` by default. See [POJO Conversion](#pojo-conversion). |

### Converter Registries

`Registries` contain the required mappings and instantiated converter instances available to the agent. Every instantiated agent uses mappings
defined in the [BaseRegistry](copperfield-core/src/main/java/dev/volix/rewinside/odyssey/common/copperfield/registry/BaseRegistry.kt) by default.

You can pass additional `Registries` to the agent at construction time. This will [combine the registries](#combine-registries) and discard the
original instances afterwards.

```java
Registry registry1 = new CustomRegistry();
Registry registry2 = new AnotherCustomRegistry();
// ...
CopperfieldAgent agent = new CopperfieldAgent(registry, anotherCustomRegistry);
```

#### Add Converters

You may want to add additional converters. This can be achieved through calling the `with()` method on the registry.

```java
Registry registry = new BaseRegistry();

registry.with(UUID.class, UuidToUpperCaseStringConverter.class); // The registry will create a new instance on demand.
// or
registry.with(UUID.class, UuidToUpperCaseStringConverter()); // Injects a specific converter instance.
```

When working with the agent and you want to add converters dynamically, you can retrieve the agent's registry and call `with()` on that too.

```java
CopperfieldAgent agent = // ...
agent.getRegistry().with(UUID.class, UuidToUpperCaseStringConverter.class);
```

**Note**: If another converter already exists for the given type (e.g. `UUID`), the previous one will be overridden.

#### Remove converters

To remove converters, you can use the `without()` method.

```java
Registry registry = // ...
registry.without(UUID.class); // Removes the converter currently assigned to UUIDs.
```

#### Combine Registries

For convenience, multiple `Registries` can be merged into a single one. This can be achieved using the corresponding `with()` method.

```java
Registry baseRegistry = new BaseRegistry();
Registry protoRegistry = new ProtoRegistry();
baseRegistry.with(protoRegistry);
```

In this example, all converter mappings and instances defined in the `protoRegistry` will be _copied_ to the `baseRegistry`. This will override
existing mappings in the `baseRegistry` if the same types are defined in both registries.

Accordingly, there is a `without()` method as well.

```java
CopperfieldAgent agent = new CopperfieldAgent(new ProtoRegistry());
agent.getRegistry().without(new BaseRegistry());
```

In this example, all converter mappings and instances defined in the `BaseRegistry` will be removed from the agent's registry if both types and
mapped converter classes are exactly the same.

### Conversion Context

TODO
* Context
* converter context
* agent context

### POJO Conversion

TODO
* CopperConvertable
* CopperField / CopperFields / CopperIgnore
* Iterables / Maps

### Bson Conversion

TODO

### Proto Conversion

TODO

## Advanced Usage

### Custom Converters

TODO

### Type Mappers

TODO
