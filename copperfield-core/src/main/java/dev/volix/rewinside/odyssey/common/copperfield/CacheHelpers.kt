package dev.volix.rewinside.odyssey.common.copperfield

import com.google.common.cache.Cache

/**
 * @author Benedikt Wüller
 */

fun <K, V> Cache<K, V>.getOrPut(key: K, compute: (K) -> V): V {
    val value = this.getIfPresent(key)
    if (value != null) return value

    val computedValue = compute(key)
    this[key] = computedValue
    return computedValue
}

fun <K, V> Cache<K, V>.clear() {
    this.invalidateAll()
}

operator fun <K, V> Cache<K, V>.set(key: K, value: V?) {
    if (value == null) {
        this.invalidate(key)
    } else {
        this.put(key, value)
    }
}
