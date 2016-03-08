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

import java.util.Scanner;
import java.util.function.Function;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Replace object that is able to apply specific line transformation for a
 * given text.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class Replace {
    /**
     * Text text.
     */
    private final transient String text;

    /**
     * Ctor.
     * @param content Raw text text.
     */
    public Replace(final String content) {
        this.text = content;
    }

    /**
     * Apply replace transformation on selected line only if matching
     * function returned true.
     *
     * @param matching Matching function.
     * @param transformation Line transformation.
     * @return Transformed file.
     */
    public Replace replace(
        final Function<String, Boolean> matching,
        final Function<String, String> transformation
    ) {
        final StrBuilder out = new StrBuilder();
        try (Scanner scanner = new Scanner(this.text)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (matching.apply(line)) {
                    out.append(transformation.apply(line));
                } else {
                    out.append(line);
                }
                out.appendNewLine();
            }
        }
        return new Replace(out.build());
    }

    /**
     * Return final version of text.
     *
     * @return Final version of text.
     */
    public String output() {
        return this.text;
    }

}
