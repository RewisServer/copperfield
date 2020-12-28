package dev.volix.rewinside.odyssey.common.copperfield.converter

import java.lang.reflect.Method

/**
 * @author Benedikt Wüller
 */
abstract class ReflectionConverter<S : Any, T : Any>(protected val type: Class<T>) : Converter<S, T> {

    private val getterCache = mutableMapOf<String, Method>()
    private val setterCache = mutableMapOf<String, Method>()

    override fun convertFrom(name: String, source: S) = this.getGetterMethod(name, source.javaClass).invoke(source) as T?

    override fun convertTo(name: String, value: T?, target: S) {
        this.getSetterMethod(name, target.javaClass).invoke(target, value)
    }

    protected open fun getGetterMethod(name: String, type: Class<S>) = this.getterCache.getOrPut(name) {
        type.getDeclaredMethod(this.getGetterMethodName(name))
    }

    protected open fun getSetterMethod(name: String, type: Class<S>) = this.setterCache.getOrPut(name) {
        type.getDeclaredMethod(this.getSetterMethodName(name), this.type)
    }

    protected abstract fun getGetterMethodName(name: String): String

    protected abstract fun getSetterMethodName(name: String): String

}
