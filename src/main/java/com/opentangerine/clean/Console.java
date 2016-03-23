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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * User interface.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class Console {
    /**
     * Output.
     */
    private final transient PrintWriter out = new PrintWriter(
        new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
    );

    /**
     * Display help file.
     */
    public void help() {
        final String path = "/ot-clean/help.txt";
        try {
            final String version = IOUtils.toString(
                getClass().getResourceAsStream("/version.txt")
            );
            // FIXME GG: in progress, add another method that is going to do this better
            // FIXME GG: in progress, add test case for this
            // FIXME GG: in progress, cleanup
            final String output = new Replace(IOUtils.toString(getClass().getResourceAsStream(path))).replace(
                it -> it.contains("{{version}}"),
                it -> StringUtils.replace(it, "{{version}}", version)
            ).output();
            this.out.append(
                output
            );
            this.out.flush();
        } catch (final IOException exc) {
            throw new IllegalArgumentException(
                "Unable to file readme file", exc
            );
        }
    }
}
