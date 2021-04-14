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


internal class JsonLiteral private constructor(private val value: String) : JsonValue() {
    override val isNull: Boolean = "null" == value
    override val isTrue: Boolean
    override val isFalse: Boolean

    @Throws(Exception::class)
    public override fun write(writer: JsonWriter) {
        writer.writeLiteral(value)
    }

    override fun toString(): String {
        return value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override val isBoolean: Boolean
        get() = isTrue || isFalse

    override fun asBoolean(): Boolean {
        return if (isNull) super.asBoolean() else isTrue
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

        return with(other as JsonLiteral) { value == other.value }
    }

    companion object {
        val NULL: JsonValue = JsonLiteral("null")
        val TRUE: JsonValue = JsonLiteral("true")
        val FALSE: JsonValue = JsonLiteral("false")
    }

    init {
        isTrue = "true" == value
        isFalse = "false" == value
    }
}