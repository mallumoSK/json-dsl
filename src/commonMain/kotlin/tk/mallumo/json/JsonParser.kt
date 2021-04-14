/**
 * Copyright (c) 2013-2016 Angelo ZERR.
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

internal class JsonParser constructor(json: String) {
    private val buffer: CharArray = json.toCharArray()
    private var bufferOffset = 0
    private var index = 0
    private var fill = 0
    private var line: Int = 1
    private var lineOffset = 0
    private var current = 0
    private var captureBuffer: StringBuilder? = null
    private var captureStart: Int


    @Throws(Exception::class)
    fun parse(): JsonValue {
        read()
        skipWhiteSpace()
        val result = readValue()
        skipWhiteSpace()
        if (!isEndOfText) {
            throw error("Unexpected character")
        }
        return result
    }

    @Throws(Exception::class)
    private fun readValue(): JsonValue {
        return when (current.toChar()) {
            'n' -> readNull()
            't' -> readTrue()
            'f' -> readFalse()
            '"' -> readString()
            '[' -> readArray()
            '{' -> readObject()
            '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber()
            else -> throw expected("value")
        }
    }

    @Throws(Exception::class)
    private fun readArray(): JsonArray {
        read()
        val array = JsonArray()
        skipWhiteSpace()
        if (readChar(']')) {
            return array
        }
        do {
            skipWhiteSpace()
            try {
                array.add(readValue())
            } catch (e: Exception) {
                break
            }
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar(']')) {
            throw expected("',' or ']'")
        }
        return array
    }

    @Throws(Exception::class)
    private fun readObject(): JsonObject {
        read()
        val `object` = JsonObject()
        skipWhiteSpace()
        if (readChar('}')) {
            return `object`
        }
        val names: MutableList<String> = ArrayList()
        do {
            skipWhiteSpace()
            val name = try {
                readName()
            } catch (e: Exception) {
                break
            }
            if (names.contains(name)) {
                throw error("Duplicate key '$name'")
            } else {
                names.add(name)
            }
            skipWhiteSpace()
            if (!readChar(':')) {
                throw expected("':'")
            }
            skipWhiteSpace()
            `object`.add(name, readValue())
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar('}')) {
            throw expected("',' or '}'")
        }
        return `object`
    }

    @Throws(Exception::class)
    private fun readName(): String {
        if (current != '"'.toInt()) {
            throw expected("name")
        }
        return readStringInternal()
    }

    @Throws(Exception::class)
    private fun readNull(): JsonValue {
        read()
        readRequiredChar('u')
        readRequiredChar('l')
        readRequiredChar('l')
        return JsonValue.Companion.NULL
    }

    @Throws(Exception::class)
    private fun readTrue(): JsonValue {
        read()
        readRequiredChar('r')
        readRequiredChar('u')
        readRequiredChar('e')
        return JsonValue.Companion.TRUE
    }

    @Throws(Exception::class)
    private fun readFalse(): JsonValue {
        read()
        readRequiredChar('a')
        readRequiredChar('l')
        readRequiredChar('s')
        readRequiredChar('e')
        return JsonValue.Companion.FALSE
    }

    @Throws(Exception::class)
    private fun readRequiredChar(ch: Char) {
        if (!readChar(ch)) {
            throw expected("'$ch'")
        }
    }

    @Throws(Exception::class)
    private fun readString(): JsonValue {
        return JsonPrimitive(readStringInternal())
    }

    @Throws(Exception::class)
    private fun readStringInternal(): String {
        read()
        startCapture()
        while (current != '"'.toInt()) {
            if (current == '\\'.toInt()) {
                pauseCapture()
                readEscape()
                startCapture()
            } else if (current < 0x20) {
                throw expected("valid string character")
            } else {
                read()
            }
        }
        val string = endCapture()
        read()
        return string
    }

    @Throws(Exception::class)
    private fun readEscape() {
        read()
        when (current.toChar()) {
            '"', '/', '\\' -> captureBuffer!!.append(current.toChar())
            'b' -> captureBuffer!!.append('\b')
            //'f' -> captureBuffer!!.append('\f')
            'n' -> captureBuffer!!.append('\n')
            'r' -> captureBuffer!!.append('\r')
            't' -> captureBuffer!!.append('\t')
//            '$' -> captureBuffer!!.append('\$')
            'u' -> {
                val hexChars = CharArray(4)
                var i = 0
                while (i < 4) {
                    read()
                    if (!isHexDigit) {
                        throw expected("hexadecimal digit")
                    }
                    hexChars[i] = current.toChar()
                    i++
                }
                captureBuffer!!.append(hexChars.concatToString().toInt(16).toChar())
            }
            else -> throw expected("valid escape sequence")
        }
        read()
    }

    @Throws(Exception::class)
    private fun readNumber(): JsonValue {
        startCapture()
        readChar('-')
        val firstDigit = current
        if (!readDigit()) {
            throw expected("digit")
        }
        if (firstDigit != '0'.toInt()) {
            while (readDigit()) {
            }
        }
        readFraction()
        readExponent()
        return JsonPrimitive(endCapture())
    }

    @Throws(Exception::class)
    private fun readFraction(): Boolean {
        if (!readChar('.')) {
            return false
        }
        if (!readDigit()) {
            throw expected("digit")
        }
        while (readDigit()) {
        }
        return true
    }

    @Throws(Exception::class)
    private fun readExponent(): Boolean {
        if (!readChar('e') && !readChar('E')) {
            return false
        }
        if (!readChar('+')) {
            readChar('-')
        }
        if (!readDigit()) {
            throw expected("digit")
        }
        while (readDigit()) {
        }
        return true
    }

    @Throws(Exception::class)
    private fun readChar(ch: Char): Boolean {
        if (current != ch.toInt()) {
            return false
        }
        read()
        return true
    }

    @Throws(Exception::class)
    private fun readDigit(): Boolean {
        if (!isDigit) {
            return false
        }
        read()
        return true
    }

    @Throws(Exception::class)
    private fun skipWhiteSpace() {
        while (isWhiteSpace) {
            read()
        }
    }

    @Throws(Exception::class)
    private fun read() {
        if (index == fill) {
            if (captureStart != -1) {
                captureBuffer!!.append(buffer, captureStart, fill - captureStart)
                captureStart = 0
            }
            bufferOffset += fill
            if (fill == buffer.size) {
                fill = -1
            }
            if (fill == 0) {
                fill = buffer.size
            }

            index = 0
            if (fill == -1) {
                current = -1
                return
            }
        }
        if (current == '\n'.toInt()) {
            line++
            lineOffset = bufferOffset + index
        }
        current = buffer[index++].toInt()
    }

    private fun startCapture() {
        if (captureBuffer == null) {
            captureBuffer = StringBuilder()
        }
        captureStart = index - 1
    }

    private fun pauseCapture() {
        val end = if (current == -1) index else index - 1
        captureBuffer!!.append(buffer, captureStart, end - captureStart)
        captureStart = -1
    }

    private fun endCapture(): String {
        val end = if (current == -1) index else index - 1
        val captured: String
        if (captureBuffer!!.length > 0) {
            captureBuffer!!.append(buffer, captureStart, end - captureStart)
            captured = captureBuffer.toString()
            captureBuffer!!.setLength(0)
        } else {
            captured = buffer.concatToString(captureStart, captureStart + (end - captureStart))
        }
        captureStart = -1
        return captured
    }

    private fun expected(expected: String): ParseException {
        // if( isEndOfText() ) {
        // return error( "Unexpected end of input" );
        //}
        return error("Expected $expected")
    }

    private fun error(message: String): ParseException {
        val absIndex = bufferOffset + index
        val column = absIndex - lineOffset
        val offset = if (isEndOfText) absIndex else absIndex - 1
        return ParseException(message, offset, line, column - 1)
    }

    private val isWhiteSpace: Boolean
        get() = current == ' '.toInt() || current == '\t'.toInt() || current == '\n'.toInt() || current == '\r'.toInt()
    private val isDigit: Boolean
        get() = current >= '0'.toInt() && current <= '9'.toInt()
    private val isHexDigit: Boolean
        get() = current >= '0'.toInt() && current <= '9'.toInt() || current >= 'a'.toInt() && current <= 'f'.toInt() || current >= 'A'.toInt() && current <= 'F'.toInt()
    private val isEndOfText: Boolean
        get() = current == -1

    companion object {
        private const val MIN_BUFFER_SIZE = 10
        private const val DEFAULT_BUFFER_SIZE = 1024
    }

    init {
        captureStart = -1
    }
}