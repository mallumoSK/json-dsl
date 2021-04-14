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


internal class JsonPrimitive(string: String?) : JsonValue() {
    private val string: String
    override fun toString(): String {
        return string
    }

    @Throws(Exception::class)
    public override fun write(writer: JsonWriter) {
        if (isNumber) {
            writer.writeNumber(string)
        } else {
            writer.writeString(string)
        }
    }

    override val isNumber: Boolean
        get() = try {
            asDouble()
            true
        } catch (e: Exception) {
            false
        }

    override fun asInt(): Int {
        return string.toInt(10)
    }

    override fun asLong(): Long {
        return string.toLong(10)
    }

    override fun asFloat(): Float {
        return string.toFloat()
    }

    override fun asDouble(): Double {
        return string.toDouble()
    }

    override fun asString(): String {
        return string
    }

    override fun hashCode(): Int {
        return string.hashCode()
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

        return with(other as JsonPrimitive){ string == other.string}

    }

    init {
        if (string == null) {
            throw NullPointerException("string is null")
        }
        this.string = string
    }
}