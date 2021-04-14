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

internal open class JsonWriter(protected val writer: StringBuilder) {
    @Throws(Exception::class)
    fun writeLiteral(value: String?) {
        value ?: throw NullPointerException()
        writer.append(value)
    }

    @Throws(Exception::class)
    fun writeNumber(string: String?) {
        string ?: throw NullPointerException()
        writer.append(string)
    }

    @Throws(Exception::class)
    fun writeString(string: String) {
        writer.append('"')
        writeJsonString(string)
        writer.append('"')
    }

    @Throws(Exception::class)
    open fun writeArrayOpen() {
        writer.append('[')
    }

    @Throws(Exception::class)
    open fun writeArrayClose() {
        writer.append(']')
    }

    @Throws(Exception::class)
    open fun writeArraySeparator() {
        writer.append(',')
    }

    @Throws(Exception::class)
    open fun writeObjectOpen() {
        writer.append('{')
    }

    @Throws(Exception::class)
    open fun writeObjectClose() {
        writer.append('}')
    }

    @Throws(Exception::class)
    fun writeMemberName(name: String) {
        writer.append('"')
        writeJsonString(name)
        writer.append('"')
    }

    @Throws(Exception::class)
    open fun writeMemberSeparator() {
        writer.append(':')
    }

    @Throws(Exception::class)
    open fun writeObjectSeparator() {
        writer.append(',')
    }

    @Throws(Exception::class)
    protected fun writeJsonString(string: String) {
        string.map { getReplacementChars(it) }
            .forEach { writer.append(it) }
    }

    companion object {
        private const val CONTROL_CHARACTERS_END = 0x001f
        private const val QUOT_CHARS = "\\\""
        private const val BS_CHARS = "\\\\"
        private const val LF_CHARS = "\\n"
        private const val CR_CHARS = "\\r"
        private const val TAB_CHARS = "\\t"

        // In JavaScript, U+2028 and U+2029 characters count as line endings and must be encoded.
        // http://stackoverflow.com/questions/2965293/javascript-parse-error-on-u2028-unicode-character
        private const val UNICODE_2028_CHARS = "\\u2028"
        private const val UNICODE_2029_CHARS = "\\u2029"
        private val HEX_DIGITS = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
        )

        private fun getReplacementChars(ch: Char): String {
            if (ch > '\\') {
                if (ch < '\u2028' || ch > '\u2029') {
                    // The lower range contains 'a' .. 'z'. Only 2 checks required.
                    return ch.toString()
                }
                return if (ch == '\u2028') UNICODE_2028_CHARS else UNICODE_2029_CHARS
            }
            if (ch == '\\') {
                return BS_CHARS
            }
            if (ch > '"') {
                // This range contains '0' .. '9' and 'A' .. 'Z'. Need 3 checks to get here.
                return ch.toString()
            }
            if (ch == '"') {
                return QUOT_CHARS
            }
            if (ch.toInt() > CONTROL_CHARACTERS_END) {
                return ch.toString()
            }
            if (ch == '\n') {
                return LF_CHARS
            }
            if (ch == '\r') {
                return CR_CHARS
            }
            return if (ch == '\t') {
                TAB_CHARS
            } else "\\u00${HEX_DIGITS[ch.toInt() shr 4 and 0x000f]}${HEX_DIGITS[ch.toInt() and 0x000f]}"
        }
    }
}