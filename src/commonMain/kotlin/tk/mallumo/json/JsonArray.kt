/**
 * Copyright (c) 2013-2015 Angelo ZERR.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr></angelo.zerr>@gmail.com> - initial API and implementation
 */
package tk.mallumo.json


/**
 * Represents a JSON array, an ordered collection of JSON values.
 *
 *
 * Elements can be added using the `add(...)` methods which accept instances of
 * [JsonValue], strings, primitive numbers, and boolean values. To replace an element of an
 * array, use the `set(int, ...)` methods.
 *
 *
 *
 * Elements can be accessed by their index using [.get]. This class also supports
 * iterating over the elements in document order using an [.iterator] or an enhanced for
 * loop:
 *
 *
 * <pre>
 * for( JsonValue value : jsonArray ) {
 * ...
 * }
</pre> *
 *
 *
 * An equivalent [List] can be obtained from the method [.values].
 *
 *
 *
 * Note that this class is **not thread-safe**. If multiple threads access a
 * `JsonArray` instance concurrently, while at least one of these threads modifies the
 * contents of this array, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 *
 *
 *
 * This class is **not supposed to be extended** by clients.
 *
 */
// use default serial UID
internal class JsonArray : JsonValue, Iterable<JsonValue?> {
    private val values: MutableList<JsonValue>

    /**
     * Creates a new empty JsonArray.
     */
    constructor() {
        values = ArrayList()
    }

    /**
     * Creates a new JsonArray with the contents of the specified JSON array.
     *
     * @param array
     * the JsonArray to get the initial contents from, must not be `null`
     */
    constructor(array: JsonArray?) : this(array, false) {}
    private constructor(array: JsonArray?, unmodifiable: Boolean) {
        if (array == null) {
            throw NullPointerException("array is null")
        }
        values = if (unmodifiable) {
            array.values.toMutableList()
        } else {
            ArrayList(array.values)
        }
    }

    /**
     * Appends the JSON representation of the specified `int` value to the end of this
     * array.
     *
     * @param value
     * the value to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: Int): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the JSON representation of the specified `long` value to the end of this
     * array.
     *
     * @param value
     * the value to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: Long): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the JSON representation of the specified `float` value to the end of this
     * array.
     *
     * @param value
     * the value to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: Float): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the JSON representation of the specified `double` value to the end of this
     * array.
     *
     * @param value
     * the value to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: Double): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the JSON representation of the specified `boolean` value to the end of this
     * array.
     *
     * @param value
     * the value to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: Boolean): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the JSON representation of the specified string to the end of this array.
     *
     * @param value
     * the string to add to the array
     * @return the array itself, to enable method chaining
     */
    fun add(value: String?): JsonArray {
        values.add(JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends the specified JSON value to the end of this array.
     *
     * @param value
     * the JsonValue to add to the array, must not be `null`
     * @return the array itself, to enable method chaining
     */
    fun add(value: JsonValue?): JsonArray {
        if (value == null) {
            throw NullPointerException("value is null")
        }
        values.add(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified `int` value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: Int): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified `long` value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: Long): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified `float` value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: Float): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified `double` value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: Double): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified `boolean` value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: Boolean): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the JSON representation of
     * the specified string.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the string to be stored at the specified array position
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: String?): JsonArray {
        values[index] = JsonValue.Companion.valueOf(value)
        return this
    }

    /**
     * Replaces the element at the specified position in this array with the specified JSON value.
     *
     * @param index
     * the index of the array element to replace
     * @param value
     * the value to be stored at the specified array position, must not be `null`
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun set(index: Int, value: JsonValue?): JsonArray {
        if (value == null) {
            throw NullPointerException("value is null")
        }
        values[index] = value
        return this
    }

    /**
     * Removes the element at the specified index from this array.
     *
     * @param index
     * the index of the element to remove
     * @return the array itself, to enable method chaining
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    fun remove(index: Int): JsonArray {
        values.removeAt(index)
        return this
    }

    /**
     * Returns the number of elements in this array.
     *
     * @return the number of elements in this array
     */
    fun size(): Int {
        return values.size
    }

    /**
     * Returns `true` if this array contains no elements.
     *
     * @return `true` if this array contains no elements
     */
    val isEmpty: Boolean
        get() = values.isEmpty()

    /**
     * Returns the value of the element at the specified position in this array.
     *
     * @param index
     * the index of the array element to return
     * @return the value of the element at the specified position
     * @throws IndexOutOfBoundsException
     * if the index is out of range, i.e. `index < 0` or
     * `index >= size`
     */
    operator fun get(index: Int): JsonValue {
        return values[index]
    }

    /**
     * Returns a list of the values in this array in document order. The returned list is backed by
     * this array and will reflect subsequent changes. It cannot be used to modify this array.
     * Attempts to modify the returned list will result in an exception.
     *
     * @return a list of the values in this array
     */
    fun values(): List<JsonValue> {
        return values.toMutableList()
    }

    /**
     * Returns an iterator over the values of this array in document order. The returned iterator
     * cannot be used to modify this array.
     *
     * @return an iterator over the values of this array
     */
    override fun iterator(): MutableIterator<JsonValue?> {
        val iterator: Iterator<JsonValue> = values.iterator()
        return object : MutableIterator<JsonValue?> {
            override fun hasNext(): Boolean {
                return iterator.hasNext()
            }

            override fun next(): JsonValue {
                return iterator.next()
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }
        }
    }

    @Throws(Exception::class)
    public override fun write(writer: JsonWriter) {
        writer.writeArrayOpen()
        val iterator: Iterator<JsonValue?> = iterator()
        var first = true
        while (iterator.hasNext()) {
            if (!first) {
                writer.writeArraySeparator()
            }
            iterator.next()?.write(writer)
            first = false
        }
        writer.writeArrayClose()
    }

    override val isArray: Boolean
        get() = true

    override fun asArray(): JsonArray {
        return this
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (this::class != other::class) {
            return false
        }
        return with( other as JsonArray){
         values == other.values}
    }

    companion object {


        /**
         * Reads a JSON array from the given string.
         *
         * @param string
         * the string that contains the JSON array
         * @return the JSON array that has been read
         * @throws ParseException
         * if the input is not valid JSON
         * @throws UnsupportedOperationException
         * if the input does not contain a JSON array
         */
        fun readFrom(string: String): JsonArray {
            return JsonValue.Companion.readFrom(string)!!.asArray()
        }

        /**
         * Returns an unmodifiable wrapper for the specified JsonArray. This method allows to provide
         * read-only access to a JsonArray.
         *
         *
         * The returned JsonArray is backed by the given array and reflects subsequent changes. Attempts
         * to modify the returned JsonArray result in an `UnsupportedOperationException`.
         *
         *
         * @param array
         * the JsonArray for which an unmodifiable JsonArray is to be returned
         * @return an unmodifiable view of the specified JsonArray
         */
        fun unmodifiableArray(array: JsonArray?): JsonArray {
            return JsonArray(array, true)
        }
    }
}