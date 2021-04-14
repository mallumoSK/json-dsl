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


internal class PrettyPrinter(writer: StringBuilder) : JsonWriter(writer) {

    private var indent = 0

    @Throws(Exception::class)
    override fun writeArrayOpen() {
        indent++
        writer.append('[')
        writeNewLine()
    }

    @Throws(Exception::class)
    override fun writeArrayClose() {
        indent--
        writeNewLine()
        writer.append(']')
    }

    @Throws(Exception::class)
    override fun writeArraySeparator() {
        writer.append(',')
        writeNewLine()
    }

    @Throws(Exception::class)
    override fun writeObjectOpen() {
        indent++
        writer.append('{')
        writeNewLine()
    }

    @Throws(Exception::class)
    override fun writeObjectClose() {
        indent--
        writeNewLine()
        writer.append('}')
    }

    @Throws(Exception::class)
    override fun writeMemberSeparator() {
        writer.append(':')
        writer.append(' ')
    }

    @Throws(Exception::class)
    override fun writeObjectSeparator() {
        writer.append(',')
        writeNewLine()
    }

    @Throws(Exception::class)
    private fun writeNewLine() {
        writer.append('\n')
        for (i in 0 until indent) {
            writer.append("  ")
        }
    }
}