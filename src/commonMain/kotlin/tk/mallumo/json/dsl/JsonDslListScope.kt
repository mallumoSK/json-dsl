package tk.mallumo.json.dsl

import tk.mallumo.json.JsonValue

interface JsonDslListScope<T> {

    fun <T> jObj(block: JsonDslObjectScope<T>.() -> T): T

    fun <T> jList(block: JsonDslListScope<T>.() -> T): List<T>

    fun int(): Int?

    fun long(): Long?

    fun float(): Float?

    fun double(): Double?

    fun boolean(): Boolean?

    fun string(): String?
}

internal open class ImplJsonDslListScope<T>(private val input: JsonValue) : JsonDslListScope<T> {

    override fun <T> jObj(block: JsonDslObjectScope<T>.() -> T): T =
        block(ImplJsonDslObjectScope(input.asObject()))

    override fun <T> jList(block: JsonDslListScope<T>.() -> T): List<T> =
        input.asArray().values().map {
            block(ImplJsonDslListScope(input))
        }

    override fun int(): Int? = try {
        input.asInt()
    } catch (e: Exception) {
        null
    }

    override fun long(): Long? = try {
        input.asLong()
    } catch (e: Exception) {
        null
    }

    override fun float(): Float? = try {
        input.asFloat()
    } catch (e: Exception) {
        null
    }

    override fun double(): Double? = try {
        input.asDouble()
    } catch (e: Exception) {
        null
    }

    override fun boolean(): Boolean? = try {
        input.asBoolean()
    } catch (e: Exception) {
        null
    }

    override fun string(): String? = try {
        input.asString()
    } catch (e: Exception) {
        null
    }
}