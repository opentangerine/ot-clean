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
        this.out.append(
            new Replace(this.resource("/ot-clean/help.txt"))
                .replace("{{version}}", this.resource("/version.txt"))
                .output()
        );
        this.out.flush();
    }

    /**
     * Return content of the resource.
     *
     * @param path Resource path.
     * @return Content.
     */
    private String resource(final String path) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(path));
        } catch (final IOException exc) {
            throw new IllegalStateException(
                String.format("Unable to read resource: %s", path), exc
            );
        }
    }
}
