package dev.volix.rewinside.odyssey.common.copperfield.bson.converter

import dev.volix.rewinside.odyssey.common.copperfield.CopperConvertable
import dev.volix.rewinside.odyssey.common.copperfield.CopperTypeMapper
import dev.volix.rewinside.odyssey.common.copperfield.bson.annotation.CopperBsonField
import dev.volix.rewinside.odyssey.common.copperfield.converter.Converter
import dev.volix.rewinside.odyssey.common.copperfield.converter.CopperConvertableConverter
import org.bson.Document
import java.lang.reflect.Field

/**
 * @author Benedikt Wüller
 */
class CopperToBsonConverter : CopperConvertableConverter<Document>(Document::class.java) {

    override fun createTheirInstance(type: Class<out Any>, ourType: Class<out CopperConvertable>?): Document {
        return Document()
    }

    override fun getValue(instance: Document, name: String, type: Class<*>): Any? {
        val parts = name.split(".")
        var current = instance

        for (i in 0 until parts.lastIndex) {
            current = current.get(parts[i], Document())
        }

        return current.get(parts.last(), type)
    }

    override fun setValue(instance: Document, name: String, value: Any?, type: Class<*>) {
        val parts = name.split(".")
        var current = instance

        for (i in 0 until parts.lastIndex) {
            val key = parts[i]
            current = current.getOrPut(key) { Document() } as Document
        }

        current[parts.last()] = value
    }

    override fun getName(name: String, field: Field): String {
        val annotation = field.getDeclaredAnnotation(CopperBsonField::class.java)
        if (annotation != null && annotation.name.isNotEmpty()) return annotation.name
        return super.getName(name, field)
    }

    override fun getConverterType(type: Class<out Converter<out Any, out Any>>, field: Field): Class<out Converter<out Any, out Any>> {
        val annotation = field.getDeclaredAnnotation(CopperBsonField::class.java)
        if (annotation != null && annotation.converter != Converter::class) return annotation.converter.java
        return super.getConverterType(type, field)
    }

    override fun getTypeMapper(type: Class<out CopperTypeMapper<out CopperConvertable, out CopperConvertable>>, field: Field): Class<out CopperTypeMapper<out CopperConvertable, out CopperConvertable>> {
        val annotation = field.getDeclaredAnnotation(CopperBsonField::class.java)
        if (annotation != null && annotation.typeMapper != CopperTypeMapper::class) return annotation.typeMapper.java
        return super.getTypeMapper(type, field)
    }

}
