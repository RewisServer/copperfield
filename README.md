# copperfield

A flexible, lightweight library to do ORMs using annotations (and magic).

[[_TOC_]]

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
CopperfieldAgent david = new CopperfieldAgent();
String uuidAsString = (String) david.toTheirs(UUID.randomUUID());
UUID stringAsUuid = david.toOurs(uuidAsString, UUID.class); 
```

The agent internally selects the converter matching the type, or the classes supertype, of the given value. The following converters are available by
default. All converters support conversion in both directions by default, however there are some exceptions.

| Our Type | Their Type | Converter | Comment |
| ---------- | ----------- | --------- | ------- |
| `UUID` | `String` | `UuidToStringConverter` | Converts uuids to strings using `toString()`. |
| `Enum` | `String` | `EnumToStringConverter` | Converts enums to their name corresponding strings. |
| `OffsetDateTime` | `String` | `OffsetDateTimeToStringConverter` | Converts datetime instances to ISO 8601 strings. |
| `Number` | `Number` | `NumberConverter` | Makes sure that the number type does not get lost when converting `toOurs()`. |
| `Iterable` | `Iterable` | `IterableConverter` | Converts all values using the matching converters defined for the `CopperfieldAgent`. This will only work when converting `toTheirs` by default. See _Converting Classes_ |
| `Map` | `Map` | `MapConverter` | Converts all keys and values using the matching converters defined for the `CopperfieldAgent`. This will only work when converting `toTheirs` by default. See _Converting Classes_  |

### Converter Registries

* TODO
    * Registries
    * Add converters
    * Remove converters
    * Combine registries

### Converter Groups

* TODO
    * Context
    
### POJO Conversion

* TODO
    * CopperConvertable
    * CopperField / CopperFields / CopperIgnore
    * Iterables / Maps
    * Bson
    * Proto
