@file:Suppress("unused")

package tk.mallumo.json.dsl

import tk.mallumo.json.JsonArray
import tk.mallumo.json.JsonObject


object JsonDsl {

    fun <T> decodeObject(input: String, block: JsonDslObjectScope<T>.() -> T): T =
        block(ImplJsonDslObjectScope(JsonObject.readFrom(input)))

    fun <T> decodeList(input: String, block: JsonDslListScope<T>.() -> T): List<T> =
        JsonArray.readFrom(input)
            .values()
            .map {
                block(ImplJsonDslListScope(it))
            }
}


