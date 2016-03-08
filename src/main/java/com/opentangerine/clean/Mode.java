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

import org.apache.commons.lang3.StringUtils;

/**
 * Cleaning mode.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
@SuppressWarnings("PMD.BooleanInversion")
public final class Mode {
    /**
     * Readonly flag.
     */
    private final transient String[] arguments;

    /**
     * Cleaning mode.
     *
     * @param args Execution arguments.
     */
    public Mode(final String... args) {
        this.arguments = args;
    }

    /**
     * Cleanup mode.
     * @return True if in readonly mode
     */
    public boolean readonly() {
        return !Arg.D.within(this.arguments);
    }

    /**
     * Is recurrence mode.
     * @return True if recurrence
     */
    public boolean recurrence() {
        return Arg.R.within(this.arguments);
    }

    /**
     * Enumeration of allowed program arguments.
     */
    public enum Arg {
        /**
         * Switch read-only mode to false.
         */
        D("d"),
        /**
         * Enable recurrence mode.
         */
        R("r");

        /**
         * Label.
         */
        private String label;

        /**
         * Ctor.
         * @param lbl Argument label.
         */
        Arg(final String lbl) {
            this.label = lbl;
        }

        /**
         * Check if label exists in args list.
         * @param args Args list.
         * @return True if exists.
         */
        public boolean within(final String... args) {
            return Arg.concat(args).contains(this.label);
        }

        /**
         * Argument label.
         * @return Label value.
         */
        public String getLabel() {
            return this.label;
        }

        /**
         * Combine all arguments and remove dash.
         * @param args Args list.
         * @return Arguments as plain string.
         */
        private static String concat(final String... args) {
            return StringUtils.replaceChars(
                StringUtils.join(args), "- ", ""
            );
        }
    }
}
