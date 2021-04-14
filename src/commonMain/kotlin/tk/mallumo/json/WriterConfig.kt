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
 * Controls the formatting of the JSON output. Use one of the available constants.
 */
internal abstract class WriterConfig {
    abstract fun createWriter(writer: StringBuilder): JsonWriter

    companion object {
        /**
         * Write JSON in its minimal form, without any additional whitespace. This is the default.
         */
        val MINIMAL: WriterConfig by lazy {
            object : WriterConfig() {
                override fun createWriter(writer: StringBuilder): JsonWriter {
                    return JsonWriter(writer)
                }
            }
        }

        /**
         * Write JSON in pretty-print, with each value on a separate line and an indentation of two
         * spaces.
         */
        val PRETTY_PRINT: WriterConfig by lazy {
            object : WriterConfig() {
                override fun createWriter(writer: StringBuilder): JsonWriter {
                    return PrettyPrinter(writer)
                }
            }
        }
    }
}