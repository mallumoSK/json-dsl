package tk.mallumo.json.dsl

import tk.mallumo.json.JsonObject
import kotlin.reflect.KClass

abstract class JsonDslObjectScope<T> {

    abstract fun <T> jObj(name: String, block: JsonDslObjectScope<T>.() -> T): T

    abstract fun <T> jList(name: String, block: JsonDslListScope<T>.() -> T): List<T>

    inline operator fun <reified T : Any> get(name: String): T? = get(name, T::class)

    abstract fun <T : Any> get(name: String, clazz: KClass<T>): T?

    abstract fun asInt(name: String): Int?

    abstract fun asLong(name: String): Long?

    abstract fun asFloat(name: String): Float?

    abstract fun asDouble(name: String): Double?

    abstract fun asBoolean(name: String): Boolean?

    abstract fun asString(name: String): String?
}

internal class ImplJsonDslObjectScope<T>(private val input: JsonObject) : JsonDslObjectScope<T>() {

    override fun <T> jObj(name: String, block: JsonDslObjectScope<T>.() -> T): T {
        return block(ImplJsonDslObjectScope(input[name]?.asObject() ?: JsonObject()))
    }

    override fun <T> jList(name: String, block: JsonDslListScope<T>.() -> T): List<T> =
        input[name]
            ?.asArray()
            ?.values()
            ?.map {
                block(ImplJsonDslListScope(it))
            } ?: listOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(name: String, clazz: KClass<T>): T? = when (clazz) {
        Int::class -> asInt(name) as T?
        Long::class -> asLong(name) as T?
        Double::class -> asDouble(name) as T?
        Float::class -> asFloat(name) as T?
        Boolean::class -> asBoolean(name) as T?
        String::class -> asString(name) as T?
        else -> throw RuntimeException("variable of '$name' is not primitive")
    }

    override fun asInt(name: String): Int? = try {
        input.getInt(name, null)
    } catch (e: Exception) {
        null
    }

    override fun asLong(name: String): Long? = try {
        input.getLong(name, null)
    } catch (e: Exception) {
        null
    }

    override fun asFloat(name: String): Float? = try {
        input.getFloat(name, null)
    } catch (e: Exception) {
        null
    }

    override fun asDouble(name: String): Double? = try {
        input.getDouble(name, null)
    } catch (e: Exception) {
        null
    }

    override fun asBoolean(name: String): Boolean? = try {
        input.getBoolean(name, null)
    } catch (e: Exception) {
        null
    }

    override fun asString(name: String): String? = try {
        input.getString(name, null)
    } catch (e: Exception) {
        null
    }
}