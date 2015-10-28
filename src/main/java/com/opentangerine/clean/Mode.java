/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 opentangerine.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.opentangerine.clean;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

/**
 * Cleaning mode.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 */
@SuppressWarnings("PMD.BooleanInversion")
public final class Mode {
    /**
     * Readonly flag.
     */
    private final transient boolean readonly;

    /**
     * Cleaning mode.
     *
     * @param args Execution arguments.
     */
    public Mode(final String... args) {
        final String arguments = StringUtils.replaceChars(
            StringUtils.join(args), "- ", ""
        );
        this.readonly = !arguments.contains("d");
        if (arguments.contains("r")) {
            throw new NotImplementedException(
                "Recurrence mode not supported yet"
            );
        }
        if (StringUtils.replaceChars(arguments, "dr", "").length() > 0) {
            throw new IllegalArgumentException(
                String.format("Unrecognized argument '%s'", arguments)
            );
        }
    }

    /**
     * Cleanup mode.
     * @return True if in readonly mode
     */
    public boolean isReadonly() {
        return this.readonly;
    }
}
