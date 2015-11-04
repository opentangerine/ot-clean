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
import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * This is test suit for {@link Clean} class.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 */
public final class CleanTest {

    /**
     * Temporary dir.
     */
    @Rule
    public transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * Check how clean is working in default mode.
     */
    @Test
    public void isNotDeletingByDefault() {
        final File target = this.createMavenProject();
        MatcherAssert.assertThat(target.isDirectory(), Matchers.is(true));
        new Clean(Paths.get(this.folder.getRoot().toURI()), "").run();
        MatcherAssert.assertThat(target.isDirectory(), Matchers.is(true));
    }

    /**
     * Check how clean is working on random directory.
     */
    @Test
    public void noExceptionOnEmptyDir() {
        new Clean(Paths.get(this.folder.getRoot().toURI()), "").run();
    }

    /**
     * Check how clean is working in delete mode.
     */
    @Test
    public void deleteTargetDirectoryForMavenProject() {
        final File target = this.createMavenProject();
        MatcherAssert.assertThat(target.isDirectory(), Matchers.is(true));
        new Clean(Paths.get(this.folder.getRoot().toURI()), "-d").run();
        MatcherAssert.assertThat(target.isDirectory(), Matchers.is(false));
    }

    /**
     * Creates maven project structure.
     * @return Target directory of maven project.
     */
    private File createMavenProject() {
        try {
            final File target = this.folder.newFolder("target");
            this.folder.newFile("target/file.txt");
            this.folder.newFile("pom.xml");
            this.folder.newFile("target/file2.txt");
            return target;
        } catch (final IOException exc) {
            throw new IllegalStateException(
                "Unable to create maven project structure",
                exc
            );
        }
    }
}
