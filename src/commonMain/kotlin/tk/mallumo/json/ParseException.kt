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
 * An unchecked exception to indicate that an input does not qualify as valid JSON.
 */
// use default serial UID
internal class ParseException internal constructor(
    message: String,
    /**
     * Returns the absolute index of the character at which the error occurred. The
     * index of the first character of a document is 0.
     *
     * @return the character offset at which the error occurred, will be &gt;= 0
     */
    val offset: Int,
    /**
     * Returns the number of the line in which the error occurred. The first line counts as 1.
     *
     * @return the line in which the error occurred, will be &gt;= 1
     */
    val line: Int,
    /**
     * Returns the index of the character at which the error occurred, relative to the line. The
     * index of the first character of a line is 0.
     *
     * @return the column in which the error occurred, will be &gt;= 0
     */
    val column: Int,
) : RuntimeException("$message at $line:$column")