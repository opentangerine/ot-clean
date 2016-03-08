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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * This is test suit for {@link Yconfig} class.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class YconfigTest {

    /**
     * Check if empty file is ok for parser.
     * @throws IOException if fails.
     */
    @Test(expected = YAMLException.class)
    public void brokenFile() throws IOException {
        Yconfig.load(this.example("test-broken.yml"));
    }

    /**
     * Check if no spaces is ok.
     * @throws IOException if fails.
     */
    @Test(expected = YAMLException.class)
    public void brokenFileNoSpaces() throws IOException {
        Yconfig.load(this.example("test-broken-2.yml"));
    }

    /**
     * Check if empty file is ok for parser.
     * @throws IOException if fails.
     */
    @Test
    public void emptyFile() throws IOException {
        final Yconfig config = Yconfig.load(this.example("test-empty.yml"));
        MatcherAssert.assertThat(config, Matchers.notNullValue());
    }

    /**
     * Check if regular file is ok for parser.
     * @throws IOException if fails.
     */
    @Test
    public void fullFile() throws IOException {
        final Yconfig config = Yconfig.load(this.example("test-full.yml"));
        config
            .filesToDelete(Paths.get("."))
            .toArray(it -> new Path[0]);
        MatcherAssert.assertThat(config, Matchers.notNullValue());
    }

    /**
     * Load example file.
     * @param name Name of example file.
     * @return File.
     */
    private File example(final String name) {
        return new File(
            getClass()
                .getResource(String.format("/%s", name))
                .getFile()
        );
    }
}
