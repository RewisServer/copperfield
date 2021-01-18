package dev.volix.rewinside.odyssey.common.copperfield.benchmark.converter

import dev.volix.rewinside.odyssey.common.copperfield.Registry
import dev.volix.rewinside.odyssey.common.copperfield.benchmark.party.PartyMember
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class PartyMembersMapConverter : Converter<Map<*, *>, Iterable<*>>(Map::class.java, Iterable::class.java) {

    override fun toTheirs(value: Map<*, *>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Iterable<*>? {
        val iterableConverter = registry.getConverterByValueType(Iterable::class.java)
        return iterableConverter.toTheirs(value?.values, field, registry, type) as Iterable<*>?
    }

    override fun toOurs(value: Iterable<*>?, field: Field?, registry: Registry<*, *>, type: Class<out Map<*, *>>): Map<*, *> {
        val iterableConverter = registry.getConverterByValueType(Iterable::class.java)
        val iterable = iterableConverter.toOurs(value, field, registry, type) as Iterable<*>?
        val map = mutableMapOf<String, PartyMember>()
        iterable?.forEach { it ->
            val member = it as PartyMember
            map[member.uuid.toString()] = member
        }
        return map
    }

}
