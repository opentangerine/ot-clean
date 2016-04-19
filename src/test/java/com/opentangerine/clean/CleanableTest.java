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
import java.util.function.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * This is test suit for {@link Cleanable} ddl.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.11
 */
public final class CleanableTest {

    /**
     * Temporary dir.
     */
    @Rule
    public transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * Check behaviour {@link Cleanable.If#fileExistsWithRegExp}.
     * Should return true if specific regexp is found.
     * @throws IOException In case of exception.
     */
    @Test
    public void shouldFindExistingRegExpInTheFile() throws IOException {
        final File file = folder.newFile();
        FileUtils.write(file, "example text");
        final Function<Path, Boolean> behaviour = Cleanable.If.fileExistsWithRegExp(file.getName(), ".*example.*");
        final Boolean result = behaviour.apply(folder.getRoot().toPath());
        MatcherAssert.assertThat(
            result,
            Matchers.is(true)
        );
    }

    /**
     * Check behaviour {@link Cleanable.If#fileExistsWithRegExp}.
     * Should return false if specific text cannot be found.
     * @throws IOException In case of exception.
     */
    @Test
    public void shouldNotFindExistingRegExpInTheFile() throws IOException {
        final File file = folder.newFile();
        FileUtils.write(file, "missing");
        final Function<Path, Boolean> behaviour = Cleanable.If.fileExistsWithRegExp(file.getName(), ".*example.*");
        final Boolean result = behaviour.apply(folder.getRoot().toPath());
        MatcherAssert.assertThat(
            result,
            Matchers.is(false)
        );
    }

}
