package dev.volix.rewinside.odyssey.common.copperfield

/**
 * @author Benedikt Wüller
 */

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

fun String.camelToSnakeCase() = camelRegex.replace(this) { "_${it.value}" }.toLowerCase()

fun String.snakeToCamelCase() = snakeRegex.replace(this) { it.value.replace("_", "").toUpperCase() }

fun String.snakeToPascalCase() = this.snakeToCamelCase().capitalize()
