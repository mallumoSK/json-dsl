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
 * Represents a JSON object, a set of name/value pairs, where the names are strings and the values
 * are JSON values.
 *
 *
 * Members can be added using the `add(String, ...)` methods which accept instances of
 * [JsonValue], strings, primitive numbers, and boolean values. To modify certain values of an
 * object, use the `set(String, ...)` methods. Please note that the `add`
 * methods are faster than `set` as they do not search for existing members. On the other
 * hand, the `add` methods do not prevent adding multiple members with the same name.
 * Duplicate names are discouraged but not prohibited by JSON.
 *
 *
 *
 * Members can be accessed by their name using [.get]. A list of all names can be
 * obtained from the method [.names]. This class also supports iterating over the members in
 * document order using an [.iterator] or an enhanced for loop:
 *
 * <pre>
 * for( Member member : jsonObject ) {
 * String name = member.getName();
 * JsonValue value = member.getValue();
 * ...
 * }
</pre> *
 *
 *
 * Even though JSON objects are unordered by definition, instances of this class preserve the order
 * of members to allow processing in document order and to guarantee a predictable output.
 *
 *
 *
 * Note that this class is **not thread-safe**. If multiple threads access a
 * `JsonObject` instance concurrently, while at least one of these threads modifies the
 * contents of this object, access to the instance must be synchronized externally. Failure to do so
 * may lead to an inconsistent state.
 *
 *
 *
 * This class is **not supposed to be extended** by clients.
 *
 */
// use default serial UID
internal class JsonObject : JsonValue, Iterable<JsonObject.Member?> {
    private val names: MutableList<String>
    private val values: MutableList<JsonValue>

    private var table: HashIndexTable

    /**
     * Creates a new empty JsonObject.
     */
    constructor() {
        names = ArrayList()
        values = ArrayList()
        table = HashIndexTable()
    }

    /**
     * Creates a new JsonObject, initialized with the contents of the specified JSON object.
     *
     * @param object
     * the JSON object to get the initial contents from, must not be `null`
     */
    constructor(`object`: JsonObject?) : this(`object`, false) {}
    private constructor(`object`: JsonObject?, unmodifiable: Boolean) {
        if (`object` == null) {
            throw NullPointerException("object is null")
        }
        if (unmodifiable) {
            names = `object`.names
            values = `object`.values
        } else {
            names = ArrayList(`object`.names)
            values = ArrayList(`object`.values)
        }
        table = HashIndexTable()
        updateHashIndex()
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified `int` value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: Int): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified `long` value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: Long): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified `float` value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: Float): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified `double` value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: Double): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified `boolean` value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: Boolean): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the JSON
     * representation of the specified string.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: String?): JsonObject {
        add(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Appends a new member to the end of this object, with the specified name and the specified JSON
     * value.
     *
     *
     * This method **does not prevent duplicate names**. Calling this method with a name
     * that already exists in the object will append another member with the same name. In order to
     * replace existing members, use the method `set(name, value)` instead. However,
     * ** *add* is much faster than *set*** (because it does not need to
     * search for existing members). Therefore *add* should be preferred when constructing new
     * objects.
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add, must not be `null`
     * @return the object itself, to enable method chaining
     */
    fun add(name: String?, value: JsonValue?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        if (value == null) {
            throw NullPointerException("value is null")
        }
        table.add(name, names.size)
        names.add(name)
        values.add(value)
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified `int` value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to replace
     * @param value
     * the value to set to the member
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: Int): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified `long` value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to replace
     * @param value
     * the value to set to the member
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: Long): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified `float` value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: Float): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified `double` value. If this object does not contain a member with this name, a
     * new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: Double): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified `boolean` value. If this object does not contain a member with this name,
     * a new member is added at the end of the object. If this object contains multiple members with
     * this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: Boolean): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the JSON representation of the
     * specified string. If this object does not contain a member with this name, a new member is
     * added at the end of the object. If this object contains multiple members with this name, only
     * the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: String?): JsonObject {
        set(name, JsonValue.Companion.valueOf(value))
        return this
    }

    /**
     * Sets the value of the member with the specified name to the specified JSON value. If this
     * object does not contain a member with this name, a new member is added at the end of the
     * object. If this object contains multiple members with this name, only the last one is changed.
     *
     *
     * This method should **only be used to modify existing objects**. To fill a new
     * object with members, the method `add(name, value)` should be preferred which is much
     * faster (as it does not need to search for existing members).
     *
     *
     * @param name
     * the name of the member to add
     * @param value
     * the value of the member to add, must not be `null`
     * @return the object itself, to enable method chaining
     */
    operator fun set(name: String?, value: JsonValue?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        if (value == null) {
            throw NullPointerException("value is null")
        }
        val index = indexOf(name)
        if (index != -1) {
            values[index] = value
        } else {
            table.add(name, names.size)
            names.add(name)
            values.add(value)
        }
        return this
    }

    /**
     * Removes a member with the specified name from this object. If this object contains multiple
     * members with the given name, only the last one is removed. If this object does not contain a
     * member with the specified name, the object is not modified.
     *
     * @param name
     * the name of the member to remove
     * @return the object itself, to enable method chaining
     */
    fun remove(name: String?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        val index = indexOf(name)
        if (index != -1) {
            table.remove(index)
            names.removeAt(index)
            values.removeAt(index)
        }
        return this
    }

    /**
     * Returns the value of the member with the specified name in this object. If this object contains
     * multiple members with the given name, this method will return the last one.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @return the value of the last member with the specified name, or `null` if this
     * object does not contain a member with that name
     */
    operator fun get(name: String?): JsonValue? {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        val index = indexOf(name)
        return if (index != -1) values[index] else null
    }

    /**
     * Returns the `int` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * `int`, an exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getInt(name: String?, defaultValue: Int?): Int {
        val value = get(name)
        return value?.asInt() ?: defaultValue!!
    }

    /**
     * Returns the `long` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * `long`, an exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getLong(name: String?, defaultValue: Long?): Long {
        val value = get(name)
        return value?.asLong() ?: defaultValue!!
    }

    /**
     * Returns the `float` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * `float`, an exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getFloat(name: String?, defaultValue: Float?): Float {
        val value = get(name)
        return value?.asFloat() ?: defaultValue!!
    }

    /**
     * Returns the `double` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON number or if it cannot be interpreted as Java
     * `double`, an exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getDouble(name: String?, defaultValue: Double?): Double {
        val value = get(name)
        return value?.asDouble() ?: defaultValue!!
    }

    /**
     * Returns the `boolean` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one will be picked. If this
     * member's value does not represent a JSON `true` or `false` value, an
     * exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getBoolean(name: String?, defaultValue: Boolean?): Boolean {
        val value = get(name)
        return value?.asBoolean() ?: defaultValue!!
    }

    /**
     * Returns the `String` value of the member with the specified name in this object. If
     * this object does not contain a member with this name, the given default value is returned. If
     * this object contains multiple members with the given name, the last one is picked. If this
     * member's value does not represent a JSON string, an exception is thrown.
     *
     * @param name
     * the name of the member whose value is to be returned
     * @param defaultValue
     * the value to be returned if the requested member is missing
     * @return the value of the last member with the specified name, or the given default value if
     * this object does not contain a member with that name
     */
    fun getString(name: String?, defaultValue: String?): String? {
        val value = get(name)
        return if (value != null) value.asString() else defaultValue
    }

    /**
     * Returns the number of members (name/value pairs) in this object.
     *
     * @return the number of members in this object
     */
    fun size(): Int {
        return names.size
    }

    /**
     * Returns `true` if this object contains no members.
     *
     * @return `true` if this object contains no members
     */
    val isEmpty: Boolean
        get() = names.isEmpty()

    /**
     * Returns a list of the names in this object in document order. The returned list is backed by
     * this object and will reflect subsequent changes. It cannot be used to modify this object.
     * Attempts to modify the returned list will result in an exception.
     *
     * @return a list of the names in this object
     */
    fun names(): List<String> {
        return names
    }

    /**
     * Returns an iterator over the members of this object in document order. The returned iterator
     * cannot be used to modify this object.
     *
     * @return an iterator over the members of this object
     */
    override fun iterator(): MutableIterator<Member?> {
        val namesIterator: Iterator<String> = names.iterator()
        val valuesIterator: Iterator<JsonValue> = values.iterator()
        return object : MutableIterator<Member?> {
            override fun hasNext(): Boolean {
                return namesIterator.hasNext()
            }

            override fun next(): Member {
                val name = namesIterator.next()
                val value = valuesIterator.next()
                return Member(name, value)
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }
        }
    }

    @Throws(Exception::class)
    public override fun write(writer: JsonWriter) {
        writer.writeObjectOpen()
        val namesIterator: Iterator<String> = names.iterator()
        val valuesIterator: Iterator<JsonValue> = values.iterator()
        var first = true
        while (namesIterator.hasNext()) {
            if (!first) {
                writer.writeObjectSeparator()
            }
            writer.writeMemberName(namesIterator.next())
            writer.writeMemberSeparator()
            valuesIterator.next().write(writer)
            first = false
        }
        writer.writeObjectClose()
    }

    override val isObject: Boolean
        get() = true

    override fun asObject(): JsonObject {
        return this
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + names.hashCode()
        result = 31 * result + values.hashCode()
        return result
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

        return with(other as JsonObject) {names == other.names && values == other.values}
    }

    fun indexOf(name: String): Int {
        val index = table[name]
        return if (index != -1 && name == names[index]) {
            index
        } else names.lastIndexOf(name)
    }

    private fun updateHashIndex() {
        val size = names.size
        for (i in 0 until size) {
            table.add(names[i], i)
        }
    }

    /**
     * Represents a member of a JSON object, a pair of a name and a value.
     */
    class Member internal constructor(
        /**
         * Returns the name of this member.
         *
         * @return the name of this member, never `null`
         */
        val name: String,
        /**
         * Returns the value of this member.
         *
         * @return the value of this member, never `null`
         */
        val value: JsonValue,
    ) {

        override fun hashCode(): Int {
            var result = 1
            result = 31 * result + name.hashCode()
            result = 31 * result + value.hashCode()
            return result
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

            return with(other as Member){
                name == other.name && value == other.value
            }
        }
    }

    internal class HashIndexTable(original: HashIndexTable?=null) {
        private val hashTable = ByteArray(32) // must be a power of two


        init {
            original?.hashTable?.copyInto(hashTable, 0, hashTable.size)
        }

        fun add(name: String, index: Int) {
            val slot = hashSlotFor(name)
            if (index < 0xff) {
                // increment by 1, 0 stands for empty
                hashTable[slot] = (index + 1).toByte()
            } else {
                hashTable[slot] = 0
            }
        }

        fun remove(index: Int) {
            for (i in hashTable.indices) {
                if (hashTable[i].toInt() == index + 1) {
                    hashTable[i] = 0
                } else if (hashTable[i] > index + 1) {
                    hashTable[i]--
                }
            }
        }

        operator fun get(name: Any): Int {
            val slot = hashSlotFor(name)
            // subtract 1, 0 stands for empty
            return (hashTable[slot].toInt() and 0xff) - 1
        }

        private fun hashSlotFor(element: Any): Int {
            return element.hashCode() and hashTable.size - 1
        }
    }

    companion object {
        /**
         * Reads a JSON object from the given string.
         *
         * @param string
         * the string that contains the JSON object
         * @return the JSON object that has been read
         * @throws ParseException
         * if the input is not valid JSON
         * @throws UnsupportedOperationException
         * if the input does not contain a JSON object
         */
        fun readFrom(string: String): JsonObject {
            return JsonValue.Companion.readFrom(string)!!.asObject()
        }

        /**
         * Returns an unmodifiable JsonObject for the specified one. This method allows to provide
         * read-only access to a JsonObject.
         *
         *
         * The returned JsonObject is backed by the given object and reflect changes that happen to it.
         * Attempts to modify the returned JsonObject result in an
         * `UnsupportedOperationException`.
         *
         *
         * @param object
         * the JsonObject for which an unmodifiable JsonObject is to be returned
         * @return an unmodifiable view of the specified JsonObject
         */
        fun unmodifiableObject(`object`: JsonObject?): JsonObject {
            return JsonObject(`object`, true)
        }
    }
}